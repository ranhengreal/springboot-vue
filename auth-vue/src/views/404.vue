<script setup>

</script>

<template>
  <div class="container">
    <img src="@/assets/img/404.png" alt=""/>
    <div class="back_home"><a href="/login">返回首页</a></div>
  </div>
</template>

<style scoped>
  .container{
    display:flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
  }
  .container img{
    height: 400px;
  }
  .container .back_home a{
    color: deepskyblue;
    text-decoration: none;
  }
</style>