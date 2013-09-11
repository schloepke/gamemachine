
$(function() {
  $(document).pjax('a[data-pjax2]', '#pjax-container');
  $(document).on('pjax:timeout', function(event) {
    console.log('timeout');
    event.preventDefault();
  })
  $(document).on('pjax:error', function(event) {
    return false;
  })
  $(document).on('pjax:success', function() {
  })
  $(document).on('pjax:send', function() {
  })
  $(document).on('pjax:complete', function() {
  })
});
