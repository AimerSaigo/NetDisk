<template>
  <el-container>
    <el-header>
      <el-menu
        default-active="2"
        class="el-menu-vertical-demo"
        style="height: 100%"
      >
        <el-menu-item index="2">
          <i class="el-icon-tickets"></i>
          <span slot="title">全部文件</span>
        </el-menu-item>
        <el-menu-item index="3" class="item">
          <i class="el-icon-minus"></i>
          <span slot="title">图片</span>
        </el-menu-item>
        <el-menu-item index="3" class="item">
          <i class="el-icon-minus"></i>
          <span slot="title" class="item">文档</span>
        </el-menu-item>
        <el-menu-item index="3">
          <i class="el-icon-minus"></i>
          <span slot="title" class="item">视频</span>
        </el-menu-item>
        <el-menu-item index="3">
          <i class="el-icon-minus"></i>
          <span slot="title" class="item">音乐</span>
        </el-menu-item>
        <el-menu-item index="3">
          <i class="el-icon-minus"></i>
          <span slot="title" class="item">其他</span>
        </el-menu-item>
      </el-menu>
    </el-header>
    <el-main></el-main>
  </el-container>
</template>

<script>
export default {
  name: 'Left',
  data(){
    return {
      use: "0",
      usesize:'0'
    }
  },
  created(){
  },
  methods:{

  }
}
</script>

<style scoped>
.el-menu{
  background-color:  #f7f7f7;
  border: 0;
  /*text-align: center;*/

}
.item{
  color:#424e67;
  font-size: 14px;
}
.el-progress-bar__outer{

}
</style>
