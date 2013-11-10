class AppServersController < ApplicationController
  before_action :set_server, only: [:show, :edit, :update, :destroy]

  layout 'manage'

  # GET /servers
  # GET /servers.json
  def index
    @app_servers = AppServer.all
  end

  # GET /servers/1
  # GET /servers/1.json
  def show
    redirect_to app_servers_path
  end

  # GET /servers/new
  def new
    @app_server = AppServer.new
  end

  # GET /servers/1/edit
  def edit
    @section = params.fetch(:section,:networking).to_sym
  end

  # POST /servers
  # POST /servers.json
  def create
    @app_server = AppServer.template.dup
    @app_server.name = server_params.fetch(:name)
    respond_to do |format|
      if @app_server.save
        format.html { redirect_to @app_server, notice: 'AppServer was successfully created.' }
      else
        format.html { render action: 'new' }
      end
    end
  end

  # PATCH/PUT /servers/1
  # PATCH/PUT /servers/1.json
  def update
    respond_to do |format|
      if @app_server.update(server_params)
        format.html { redirect_to @app_server, notice: 'AppServer was successfully updated.' }
      else
        format.html { render action: 'edit' }
      end
    end
  end

  # DELETE /servers/1
  # DELETE /servers/1.json
  def destroy
    @app_server.destroy
    respond_to do |format|
      format.html { redirect_to app_servers_url }
    end
  end

  private


    # Use callbacks to share common setup or constraints between actions.
    def set_server
      @app_server = AppServer.find(params[:id])
    end

    def server_params
      params.require(:app_server).permit!
    end
end
