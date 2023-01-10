<template>
  <el-container>
    <el-header><Header></Header></el-header>
    <el-container>
      <el-aside><Aside></Aside></el-aside>
      <el-main><MainBody></MainBody></el-main>
    </el-container>
  </el-container>
</template>

<script>
import Header from '@/components/Header'
import MainBody from '@/components/MainBody'
import Aside from '@/components/Aside'
export default {
  name: 'Index',
  components: { Header, Aside, MainBody }
}
</script>

<style scoped>
.el-header{
  width: 100vw;
  box-shadow:  0 2px 6px 0 rgba(0,0,0,.05);

}
.el-container{
  height: 100vh;

}

.el-aside{
  background-color:  #f7f7f7;
  max-width: 194px;

}
.el-main{
  height: calc(100% - 62px);
  max-width: calc(100% - 194px);
}

</style>
