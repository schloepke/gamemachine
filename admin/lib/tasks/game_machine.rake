
GAME_MACHINE_ROOT = File.expand_path(File.join(Rails.root,'../server'))

def set_dirs
  @java_dir = File.join(GAME_MACHINE_ROOT, 'java')
  @gradlew = File.join(@java_dir,'gradlew')
  @java_libs = File.join(@java_dir,'lib','*.jar')
  @java_sources = File.join(@java_dir,'src','main','java','com','game_machine','entity_system','generated','*.java')
end

namespace :game_machine do

  desc 'migrate database'
  task :migrate => [:environment,'db:migrate'] do
    SystemStatus.status.ok!
  end

  namespace :config do
    desc 'pubish game config'
    task :publish => :environment do
      Export::GameConfig.publish
    end
  end

  namespace :data do
    desc 'pubish game data'
    task :publish => :environment do
      Export::GameData.new.publish
    end
  end

  namespace :protobuf do
    desc 'publish protocol buffer messages'
    task :publish => :environment do
      ProtoGenerator.publish
    end
  end

  desc 'publish data changes to server'
  task :publish => ['data:publish','protobuf:publish','config:publish'] do
  end

  namespace :server do
    desc 'update server with latest admin data (components,proto messages, config)'
    task :update => [:clean, 'data:publish','protobuf:publish','config:publish'] do
      system "cd #{@java_dir} && #{@gradlew} clean && #{@gradlew} codegen && #{@gradlew} build && #{@gradlew} install_libs"
    end

    task :clean do
      set_dirs
      FileUtils.rm_r Dir.glob(@java_libs)
      FileUtils.rm_r Dir.glob(@java_sources)
    end

    task :build do
      set_dirs
      system "cd #{@java_dir} && #{@gradlew} build && #{@gradlew} install_libs"
    end
  end


end

