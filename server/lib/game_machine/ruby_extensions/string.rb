class String
  def constantize
    self.split("::").inject(Module) {|acc, val| acc.const_get(val)}
  end

  def blank?
    self !~ /\S/
  end

  def underscore
    self.gsub(/::/, '/').
    gsub(/([A-Z]+)([A-Z][a-z])/,'\1_\2').
    gsub(/([a-z\d])([A-Z])/,'\1_\2').
    tr("-", "_").
    downcase
  end
end
