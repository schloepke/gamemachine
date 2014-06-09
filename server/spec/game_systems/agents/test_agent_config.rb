class TestAgentConfig

  def reload?
    true
  end

  def agent_names
    data.keys
  end

  def klass_for_name(name)
    data.fetch(name,nil)
  end

  def load!
  end

  def data
    {
      :agent1 => 'TestAgent',
      :agent2 => 'TestAgent',
      :agent3 => 'TestAgent',
      :agent4 => 'TestAgent',
      :agent5 => 'TestAgent'
    }
  end


end
