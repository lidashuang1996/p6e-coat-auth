import{u as v}from"./auth-f5d6d495.js";import{e as c,A as g,d as b,r,o as O,G as d,a as w,c as S,b as n,v as y,f,g as _,t as C,h as p,i as x,F as A,B as P,p as X,q as B,_ as k}from"./index-29842074.js";import{A as I}from"./auth-77f04f72.js";var N={icon:{tag:"svg",attrs:{viewBox:"64 64 896 896",focusable:"false"},children:[{tag:"path",attrs:{d:"M847.9 592H152c-4.4 0-8 3.6-8 8v60c0 4.4 3.6 8 8 8h605.2L612.9 851c-4.1 5.2-.4 13 6.3 13h72.5c4.9 0 9.5-2.2 12.6-6.1l168.8-214.1c16.5-21 1.6-51.8-25.2-51.8zM872 356H266.8l144.3-183c4.1-5.2.4-13-6.3-13h-72.5c-4.9 0-9.5 2.2-12.6 6.1L150.9 380.2c-16.5 21-1.6 51.8 25.1 51.8h696c4.4 0 8-3.6 8-8v-60c0-4.4-3.6-8-8-8z"}}]},name:"swap",theme:"outlined"};const j=N;function m(t){for(var e=1;e<arguments.length;e++){var a=arguments[e]!=null?Object(arguments[e]):{},o=Object.keys(a);typeof Object.getOwnPropertySymbols=="function"&&(o=o.concat(Object.getOwnPropertySymbols(a).filter(function(s){return Object.getOwnPropertyDescriptor(a,s).enumerable}))),o.forEach(function(s){z(t,s,a[s])})}return t}function z(t,e,a){return e in t?Object.defineProperty(t,e,{value:a,enumerable:!0,configurable:!0,writable:!0}):t[e]=a,t}var i=function(e,a){var o=m({},e,a.attrs);return c(g,m({},o,{icon:j}),null)};i.displayName="SwapOutlined";i.inheritAttrs=!1;const M=i;const V=t=>(X("data-v-faaf66d3"),t=t(),B(),t),H={key:0,class:"oauth2-reconfirm"},L={class:"oauth2-reconfirm-container"},R={class:"oauth2-reconfirm-content"},D={class:"oauth2-reconfirm-content-left"},E={class:"oauth2-reconfirm-content-center"},G={class:"oauth2-reconfirm-content-right"},$=V(()=>n("div",{class:"oauth2-reconfirm-title"}," 'XXXXXXX' ",-1)),q={class:"oauth2-reconfirm-button"},F=b({__name:"Oauth2Reconfirm",setup(t){const e=v(),a=r(!1),o=r(!1);r({reconfirm:1});const s=async()=>{o.value=!0,await e.o2r(),o.value=!1},h=()=>{d.toHomePage()};return O(async()=>{await I.init()?a.value=!0:d.toLoginPage()}),(T,J)=>{const l=A,u=P;return a.value?(w(),S("div",H,[n("div",L,[n("div",R,[n("div",D,[c(l,{size:100,src:"/images/logo.png"})]),n("div",E,[c(y(M))]),n("div",G,[c(l,{size:100,src:"/images/vite.svg"})])]),$,n("div",q,[c(u,{type:"primary",loading:o.value,onClick:p(s,["stop"]),class:"oauth2-reconfirm-button-confirm"},{default:f(()=>[_(C(o.value?"":"确定"),1)]),_:1},8,["loading","onClick"]),c(u,{disabled:o.value,onClick:p(h,["stop"]),class:"oauth2-reconfirm-button-cancel"},{default:f(()=>[_(" 取消 ")]),_:1},8,["disabled","onClick"])])])])):x("",!0)}}});const Y=k(F,[["__scopeId","data-v-faaf66d3"]]);export{Y as default};
