
GAME_MACHINE_ROOT = File.expand_path(File.join(Rails.root,'../'))

def set_dirs
  @java_dir = File.join(GAME_MACHINE_ROOT, 'java')
  @gradlew = File.join(@java_dir,'gradlew')
  @java_libs = File.join(@java_dir,'lib','*.jar')
  @java_sources = File.join(@java_dir,'src','main','java','com','game_machine','entity_system','generated','*.java')
end

namespace :game_machine do
  desc 'generate models and protobuf message for new components'
  task :update_components => :environment do
    ComponentGenerator.generate_all
    Rake::Task['db:migrate'].invoke
  end


  task :update_all => [:clean, :update_components] do
    system "cd #{@java_dir} && #{@gradlew} clean && #{@gradlew} codegen && #{@gradlew} build && #{@gradlew} install_libs"
  end

  task :clean do
    set_dirs
    FileUtils.rm_f @java_libs
    FileUtils.rm_f @java_sources
  end

  task :build do
    set_dirs
    system "cd #{@java_dir} && #{@gradlew} build && #{@gradlew} install_libs"
  end
end

