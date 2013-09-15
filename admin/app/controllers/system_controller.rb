class SystemController < ApplicationController

  def restart_game_server
    if server = Server.find_running
      server.restart
      flash[:notice] = 'Game server restarting'
    end
    redirect_to '/admin'
  end

  def stop_game_server
    if server = Server.find_running
      server.stop
      flash[:notice] = 'Game server stopping'
    end

    redirect_to '/admin'
  end

  def start_game_server
    if server = Server.find_enabled
      server.start
      flash[:notice] = 'Game server starting'
    end
    redirect_to '/admin'
  end

  def publish
    Export::GameConfig.publish
    Export::GameData.new.publish
    ProtoGenerator.publish
    if server = Server.find_running
      server.restart
    end
    flash[:notice] = 'Game data published'
    redirect_to '/admin'
  end

  def recompile
    system("cd #{Rails.root};bin/rails.sh update_game_server")
    redirect_to '/admin'
  end
end
