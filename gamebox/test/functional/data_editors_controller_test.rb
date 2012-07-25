require 'test_helper'

class DataEditorsControllerTest < ActionController::TestCase
  setup do
    @data_editor = data_editors(:one)
  end

  test "should get index" do
    get :index
    assert_response :success
    assert_not_nil assigns(:data_editors)
  end

  test "should get new" do
    get :new
    assert_response :success
  end

  test "should create data_editor" do
    assert_difference('DataEditor.count') do
      post :create, data_editor: {  }
    end

    assert_redirected_to data_editor_path(assigns(:data_editor))
  end

  test "should show data_editor" do
    get :show, id: @data_editor
    assert_response :success
  end

  test "should get edit" do
    get :edit, id: @data_editor
    assert_response :success
  end

  test "should update data_editor" do
    put :update, id: @data_editor, data_editor: {  }
    assert_redirected_to data_editor_path(assigns(:data_editor))
  end

  test "should destroy data_editor" do
    assert_difference('DataEditor.count', -1) do
      delete :destroy, id: @data_editor
    end

    assert_redirected_to data_editors_path
  end
end
