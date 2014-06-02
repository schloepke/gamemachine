require 'set'
module GameMachine
  module Actor
    class Reloadable

      class << self

        def paths
          if @paths
            @paths
          else
            @paths = java.util.concurrent.ConcurrentHashMap.new
          end
        end

        def registered_actors
          if @registered_actors
            @registered_actors
          else
            @registered_actors = java.util.concurrent.ConcurrentHashMap.new
          end
        end

        def register(name)
          registered_actors[File.basename(name.underscore)] = name
        end

        def reload_actor(basename)
          if klassname = registered_actors.fetch(basename,nil)
            Base.find(klassname).tell('reload')
          end
        end

        def update_paths(first_run=false)
          Dir.glob(File.join(GameLoader.games_root,'**','*.rb')).each do |file|
            mtime = File.mtime(file)
            basename = File.basename(file,'.rb')

            # existing file
            if fileinfo = paths.fetch(basename,nil)
              if mtime != fileinfo[:mtime]
                fileinfo[:changed] = true
                fileinfo[:mtime] = mtime
                GameMachine.logger.info "Game actor #{basename} changed"
                reload_actor(basename)
              end

            # new file
            else
              File.open(file,'rb') do |f|
                str = f.read
                if str.match(/Actor\:\:Base/)
                  fileinfo = {:changed => true, :file => file, :mtime => mtime}
                  if first_run
                    fileinfo[:changed] = false
                  end
                  GameMachine.logger.info "Game actor #{basename} added to watch list changed=#{fileinfo[:changed]}"
                  paths[basename] = fileinfo
                end
              end
            end

          end
        end

        def actor_changed?(name)
          path = File.basename(name.underscore)
          if fileinfo = paths.fetch(path,nil)
            if fileinfo[:changed]
              return true
            end
          end
          false
        end

        def reload_if_changed(name)
          filename = File.basename(name.underscore)
          if fileinfo = paths.fetch(filename,nil)
            if fileinfo[:changed]
              GameMachine.logger.info "Reloading #{name} (#{fileinfo[:file]})"
              path = fileinfo[:file]
              klassname = name.split('::').last
              module_name = name.sub("::#{klassname}",'')
              module_name.constantize.send(:remove_const,klassname.to_sym)
              load "#{path}"
              fileinfo[:changed] = false
            else
              GameMachine.logger.info "Not reloading #{name} as it has not changed"
            end
          else
            GameMachine.logger.info "#{filename} not found in watch list"
          end
        end

      end
    end
  end
end
