require 'spec_helper'
require 'benchmark'
module GameMachine

  describe "couchproxy integration test" do
    
    subject {JavaLib::Couchclient.get_instance}

    before(:each) do
      JavaLib::Couchclient.get_instance.set_credentials(
          Application.config.gamecloud.host,
          Application.config.gamecloud.user,
          Application.config.gamecloud.api_key
        )
    end

    it "should handle string values" do
      subject.put_string("key1","value")
      expect(subject.get_string("key1")).to eql 'value'
    end

    it "should handle keys that require uri escaping" do
      key = 'first|two|=()*? $%#{@!~}"last'
      value = "testing"
      subject.put_string(key,value)
      expect(subject.get_string(key)).to eql value
    end

    it "should handle protobuf binary messages" do
      key = "one"
      entity = MessageLib::Entity.new.set_id("testing")
      subject.put_bytes(key,entity.to_byte_array)
      bytes = subject.get_bytes(key)
      entity = MessageLib::Entity.parse_from(bytes)
      expect(entity.id).to eql "testing"
    end
  end
end