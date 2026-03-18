<template>
<div style="padding: 15px">
  <el-card>
    <template #header>
      <div class="card-header">
        <span>首页</span>
      </div>
    </template>
  </el-card>
</div>
</template>

<script setup>
import request from "@/util/request.js";

request.get('/user/list').then(res=>{
  console.log(res);
});
</script>

<style scoped>

</style>