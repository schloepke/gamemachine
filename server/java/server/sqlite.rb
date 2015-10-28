
CREATE TABLE <%= klass.underscore.pluralize %> (
  
  <% message.getFields.each do |field| %>
  <% next if field.name == 'recordId' %>
  <%= sql_field(klass,field,'sqlite') %>
  <% end %>
  
  <% message_fields.each_with_index do |message_field,i| %>
    <% klass_message = messages_index[message_field] %>
    <% klass_message.getFields.each do |field| %>
      <%= message_field_names[i] %>_<%= sql_field(message_field,field,'sqlite',true) %>
    <% end %>
  <% end %>
id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL
);