require 'spec_helper'

module GameMachine

  describe "jmonkey engine" do
    pending do
    it "works" do
      Jme::Natives.extractNativeLib("linux", "bulletjme64", true, false)
      app = Physics::JmeApp.new
      app.start(Jme::JmeContext::Type::Headless)
      app.wait_for_start
      body = Physics::JmeBody.new
      app.add_geometry(body.geometry)
      app.add_geometry(body.geometry)
      sleep 2
      app.stop
    end
    end
  end
end
