<template>
  <a-layout>
    <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
      <div class="about">
        <h2>我是XX，XXXXXX，XXXXXXX</h2>
        <div><b>XXXXXXXXX，XXXXXXXXXXXXX</b></div>
        <div>
          <b>XXXXXX：
            <a href="https://coding.imooc.com/class/474.html" target="_blank">
              《XXXXXXXXXXXXXXXXX XXXXXXXXXXXX XXXXXXXXXXXX》
            </a>
          </b>
        </div>
        <div>
          <b>XXXXXX：
            <a href="https://coding.imooc.com/class/416.html" target="_blank">
              《XXXXXXXXXXXXXXX XXXXXXXXXXXXXXXXXXX XXXXXXXX》
            </a>
          </b>
        </div>
        <div>
          <b>XXXXXXXXXXX：
            <a href="https://www.imooc.com/learn/1160" target="_blank">
              《XXXXXXXXXXXXXXXXXXXXXXXX》
            </a>
          </b>
        </div>
        <br>
        <div><b>QQ：XXXXXXXX，<span style="color: rgb(194, 79, 74)">阿里云产品使用咨询、购买优惠</span>，加QQ，备注：阿里云</b></div>
        <div><b>微信号：XXXXXX</b></div>
        <div><b>QQ群：XXXXXX，Java全栈交流群</b></div>
        <div><b>个人全栈技术博客：
          <a href="http://www.jiawablog.com" target="_blank">XX博客</a></b>
        </div>
        <div style="color: rgb(194, 79, 74)"><b>公众号：XX全栈，扫码关注</b></div>
        <div>
          <img src="/image/jiawaquanzhan.jpg">
        </div>
      </div>
    </a-layout-content>
  </a-layout>
</template>

<style>
  .about {
    line-height: 30px;
    padding: 40px;
    background-color: #f5f5f5;
    font-size: 16px;
  }
  .about h2 {
    margin-bottom: 30px;
    font-weight: 700;
  }
  .about img {
    margin-top: 10px;
  }
</style>
