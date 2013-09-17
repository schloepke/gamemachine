class SystemController < ApplicationController

  skip_before_filter :check_system_status, :only => [:action_required, :clear_status, :test]

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

  def restart
    FileUtils.touch '/tmp/restart.txt'
  end

  def test
    render :text => SystemStatus.status.status_text
  end

  def recompile
    system("cd #{Rails.root}/../;bin/admin.sh update_game_server")
    redirect_to '/admin'
  end

  def action_required
    render :layout => false
  end

  def clear_status
    SystemStatus.status.ok!
    redirect_to '/admin'
  end
end
