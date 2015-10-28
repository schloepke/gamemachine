
DROP TABLE IF EXISTS <%= klass.underscore.pluralize %>;
CREATE TABLE <%= klass.underscore.pluralize %> (
  id bigserial NOT NULL,
  <% message.getFields.each do |field| %>
  <% next if field.name == 'recordId' %>
  <%= sql_field(klass,field,'postgres') %>
  <% end %>
  
  <% message_fields.each_with_index do |message_field,i| %>
    <% klass_message = messages_index[message_field] %>
    <% klass_message.getFields.each do |field| %>
     <%= message_field_names[i] %>_<%= sql_field(message_field,field,'postgres',true) %>
    <% end %>
  <% end %>
  
  CONSTRAINT <%=klass.underscore%>_pkey PRIMARY KEY (id)
);
alter table <%= klass.underscore.pluralize %> owner to gamemachine;