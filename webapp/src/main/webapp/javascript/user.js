$(function () {

  var indexes = {
    preferences:0,
    alerts:1,
    searchalerts:2
  };

  var activeIndex = indexes[getParameterByName("tabId")] || 0;

  //setup tabs
  $("#user-forms").tabs();

  var $panes = $(this).find('div.tab-pane');
  var $tab_nav = $(this).find('div.tab-nav');
  var $tab_lis = $tab_nav.find('li');

  $tab_lis.removeClass('active');
  $panes.hide();

  $tab_lis.eq(activeIndex).addClass('active');
  $panes.eq(activeIndex).show();

  //checkboxes on the alerts form
  $("#checkAllWeekly").change(function () {
    $("li.alerts-weekly input").not(":first")
      .attr("checked", $(this).is(":checked"));
  });
  $("#checkAllMonthly").click(function () {
    $("li.alerts-monthly input").not(":first")
      .attr("checked", $(this).is(":checked"));
  });
});
