class SystemController < ApplicationController

  def restart
    system("cd #{Rails.root};bin/rails.sh restart &")
    redirect_to '/admin'
  end

  def publish
    system("cd #{Rails.root};bin/rails.sh publish_game_data")
    redirect_to '/admin'
  end

  def recompile
    system("cd #{Rails.root};bin/rails.sh update_game_server")
    redirect_to '/admin'
  end
end
