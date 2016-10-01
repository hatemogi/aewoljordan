(ns aewoljordan.www.boot
  {:boot/export-tasks true}
  (:require [clojure.java.io :as io]
            [clojure.string :as s]
            [boot.core :as b]
            [boot.task.built-in :as task]
            [boot.util :refer [info]]
            [hiccup.core :refer [h]]
            [hiccup.page :refer [html5 include-css include-js]]
            [markdown.core]))

(defn layout
  "HTML 기본 레이아웃"
  [& contents]
  (html5 [:head
          [:meta {:charset "utf-8"}]
          [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
          [:meta {:name "google-site-verification" :content "se4ZqdtW8-jeEFu5Wt9q6PhaQIUZw1zIQxJ7MxbUaFY"}]
          [:title "애월조단 - 안전 바이크 라이딩"]
          (map include-css
               ["https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
                "https://maxcdn.bootstrapcdn.com/font-awesome/4.6.3/css/font-awesome.min.css"
                "http://cdnjs.cloudflare.com/ajax/libs/highlight.js/9.6.0/styles/default.min.css"
                "css/cover.css"
                "css/ohucode.css?20160904"])]
         (-> [:body]
             (into contents)
             (into (map include-js ["https://code.jquery.com/jquery-3.1.0.min.js"
                                    "https://d3js.org/d3.v4.min.js"
                                    "http://cdnjs.cloudflare.com/ajax/libs/highlight.js/9.6.0/highlight.min.js"
                                    "https://apis.google.com/js/platform.js"
                                    "js/aewoljordan.js?20160904"])))))

(defn cover
  "커버 페이지 레이아웃"
  [& contents]
  (layout [:div.site-wrapper
           [:div.site-wrapper-inner
            [:div.cover-container
             [:div.masthead.clearfix
              [:div.inner
               [:h3.masthead-brand "애월조단"]
               [:nav.masthead-nav
                [:ul.nav.masthead-nav
                 [:li [:a {:href "index.html"} "소개"]]
                 [:li [:a {:href "bikes.html"} "바이크"]]
                 [:li [:a {:href "contact.html"} "연락처"]]]]]]
             [:div.inner.cover (into [:main] contents)]
             [:div.mastfoot
              [:div.inner
               [:div.g-ytsubscribe {:data-channelid "UCX28LpDPiGlRHZ3WDWg8D9A" :data-layout "full" :data-count "hidden" :data-theme "dark"}]
               ]]]]]))

(defn md->html "마크다운을 HTML로 변환"
  [text]
  (markdown.core/md-to-html-string
   text
   :reference-links? true
   :footnotes? true))

(defn convert [f in out]
  (io/make-parents out)
  (spit out (f (slurp in))))

(b/deftask markdown
  "마크다운 페이지 변환"
  []
  (let [tmp (b/tmp-dir!)]
    (b/with-pre-wrap fileset
      (doseq [md (b/by-ext [".md" ".markdown"] (b/input-files fileset))]
        (let [in-path  (b/tmp-path md)
              out-path (s/replace in-path #"\.(md|markdown)$" ".html")
              out-file (io/file tmp out-path)]
          (info "%s => %s\n" in-path out-path)
          (convert (comp cover md->html)
                   (b/tmp-file md) out-file)))
      (b/commit! (b/add-resource fileset tmp)))))

(def ^:private about-content
  [:div [:h1 "애월조단"]
   [:img.profile {:src "img/profile.jpg"}]])

(b/deftask about
  "소개 페이지 생성: 후에 html이 직접 필요하면 씁시다."
  []
  (let [tmp (b/tmp-dir!)]
    (b/with-pre-wrap fileset
      (let [out-file (io/file tmp "about.html")]
        (info "generating about.html\n")
        (spit out-file (cover about-content)))
      (b/commit! (b/add-resource fileset tmp)))))

(b/deftask build
  "빌드 태스크"
  []
  (comp (markdown) (task/target)))
