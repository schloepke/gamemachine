class DataEditorsController < ApplicationController
  # GET /data_editors
  # GET /data_editors.json
  def index
    @data_editors = DataEditor.all

    respond_to do |format|
      format.html # index.html.erb
      format.json { render json: @data_editors }
    end
  end

  # GET /data_editors/1
  # GET /data_editors/1.json
  def show
    @data_editor = DataEditor.find(params[:id])

    respond_to do |format|
      format.html # show.html.erb
      format.json { render json: @data_editor }
    end
  end

  # GET /data_editors/new
  # GET /data_editors/new.json
  def new
    @data_editor = DataEditor.new

    respond_to do |format|
      format.html # new.html.erb
      format.json { render json: @data_editor }
    end
  end

  # GET /data_editors/1/edit
  def edit
    @data_editor = DataEditor.find(params[:id])
  end

  # POST /data_editors
  # POST /data_editors.json
  def create
    @data_editor = DataEditor.new(params[:data_editor])

    respond_to do |format|
      if @data_editor.save
        format.html { redirect_to @data_editor, notice: 'Data editor was successfully created.' }
        format.json { render json: @data_editor, status: :created, location: @data_editor }
      else
        format.html { render action: "new" }
        format.json { render json: @data_editor.errors, status: :unprocessable_entity }
      end
    end
  end

  # PUT /data_editors/1
  # PUT /data_editors/1.json
  def update
    @data_editor = DataEditor.find(params[:id])

    respond_to do |format|
      if @data_editor.update_attributes(params[:data_editor])
        format.html { redirect_to @data_editor, notice: 'Data editor was successfully updated.' }
        format.json { head :no_content }
      else
        format.html { render action: "edit" }
        format.json { render json: @data_editor.errors, status: :unprocessable_entity }
      end
    end
  end

  # DELETE /data_editors/1
  # DELETE /data_editors/1.json
  def destroy
    @data_editor = DataEditor.find(params[:id])
    @data_editor.destroy

    respond_to do |format|
      format.html { redirect_to data_editors_url }
      format.json { head :no_content }
    end
  end
end
