/* 하이라이트JS */
(function ($) {
    hljs.initHighlightingOnLoad();
})(jQuery);

/* 구글 어날리틱스 */
(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
})(window,document,'script','https://www.google-analytics.com/analytics.js','ga');

ga('create', 'UA-80737664-1', 'auto');
ga('send', 'pageview');

/* 유튜브 구독 */
function onYtEvent(payload) {
    if (payload.eventType == 'subscribe') {
        // Add code to handle subscribe event.
    } else if (payload.eventType == 'unsubscribe') {
        // Add code to handle unsubscribe event.
    }
    if (window.console) { // for debugging only
        window.console.log('YT event: ', payload);
    }
}
