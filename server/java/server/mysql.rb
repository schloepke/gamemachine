
DROP TABLE IF EXISTS <%= klass.underscore.pluralize %>;
CREATE TABLE `<%= klass.underscore.pluralize %>` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  <% message.getFields.each do |field| %>
  <% next if field.name == 'recordId' %>
  <%= sql_field(klass,field,'mysql') %>
  <% end %>
  
  <% message_fields.each_with_index do |message_field,i| %>
    <% klass_message = messages_index[message_field] %>
    <% klass_message.getFields.each do |field| %>
      <%= message_field_names[i] %>_<%= sql_field(message_field,field,'mysql',true) %>
    <% end %>
  <% end %>
  
  PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;