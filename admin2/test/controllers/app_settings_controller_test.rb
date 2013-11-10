require 'test_helper'

class AppSettingsControllerTest < ActionController::TestCase
  setup do
    @app_setting = app_settings(:one)
  end

  test "should get index" do
    get :index
    assert_response :success
    assert_not_nil assigns(:app_settings)
  end

  test "should get new" do
    get :new
    assert_response :success
  end

  test "should create app_setting" do
    assert_difference('AppSetting.count') do
      post :create, app_setting: { cache_write_interval: @app_setting.cache_write_interval, cache_writes_per_second: @app_setting.cache_writes_per_second, couchbase_servers: @app_setting.couchbase_servers, data_store: @app_setting.data_store, environment: @app_setting.environment, game_handler: @app_setting.game_handler, mono_enabled: @app_setting.mono_enabled, singleton_manager_router_count: @app_setting.singleton_manager_router_count, singleton_manager_update_interval: @app_setting.singleton_manager_update_interval, world_grid_cell_size: @app_setting.world_grid_cell_size, world_grid_size: @app_setting.world_grid_size }
    end

    assert_redirected_to app_setting_path(assigns(:app_setting))
  end

  test "should show app_setting" do
    get :show, id: @app_setting
    assert_response :success
  end

  test "should get edit" do
    get :edit, id: @app_setting
    assert_response :success
  end

  test "should update app_setting" do
    patch :update, id: @app_setting, app_setting: { cache_write_interval: @app_setting.cache_write_interval, cache_writes_per_second: @app_setting.cache_writes_per_second, couchbase_servers: @app_setting.couchbase_servers, data_store: @app_setting.data_store, environment: @app_setting.environment, game_handler: @app_setting.game_handler, mono_enabled: @app_setting.mono_enabled, singleton_manager_router_count: @app_setting.singleton_manager_router_count, singleton_manager_update_interval: @app_setting.singleton_manager_update_interval, world_grid_cell_size: @app_setting.world_grid_cell_size, world_grid_size: @app_setting.world_grid_size }
    assert_redirected_to app_setting_path(assigns(:app_setting))
  end

  test "should destroy app_setting" do
    assert_difference('AppSetting.count', -1) do
      delete :destroy, id: @app_setting
    end

    assert_redirected_to app_settings_path
  end
end
