
CREATE TABLE <%= klass.underscore.pluralize %> (
  
  <% message.getFields.each do |field| %>
  <% next if field.name == 'recordId' %>
  <%= sql_field(klass,field,'sqlite') %>
  <% end %>
  
  <% message_fields.each do |message_field| %>
    <% klass_message = messages_index[message_field] %>
    <% klass_message.getFields.each do |field| %>
  <%= sql_field(message_field,field,'sqlite',true) %>
    <% end %>
  <% end %>
id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL
);