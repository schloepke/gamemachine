module ApplicationHelper

  def static_nav
      li_stack = RailsAdmin::Config.navigation_static_links.map do |title, url|
        content_tag(:li, link_to(title.to_s, url, :data => { :'pjax2' => '' }))
      end.join

      label = RailsAdmin::Config.navigation_static_label || t('admin.misc.navigation_static_label')
      li_stack = %{<li class='nav-header'>#{label}</li>#{li_stack}}.html_safe if li_stack.present?
      li_stack
  end
end
