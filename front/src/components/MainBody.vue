<template>
  <div>
    <div class="workplace">
      <div class="body">
        <!--    上方工具栏-->
        <el-button v-if="path=='/'+username+'/'?false:true" type="text" size="medium" icon="el-icon-arrow-left" @click="goback">返回上一级</el-button>
        <el-button type="primary" size="medium" icon="el-icon-upload2" @click="uploadDialogVisible = true">上传文件</el-button>
        <el-button size="medium" @click="newDialogVisible= true">新建文件夹</el-button>
        <!--    新建文件夹窗口-->
        <el-dialog title="新建文件夹" :visible.sync=" newDialogVisible" width="20%">
          <el-input v-model="input" placeholder="请输入文件夹名称" />
          <span slot="footer" class="dialog-footer">
            <el-button @click="newDialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="newfolder">确 定</el-button>
          </span>
        </el-dialog>
        <!--    上传文件窗口-->
        <el-dialog title="上传" :visible.sync="uploadDialogVisible" width="30%">
          <el-upload v-loading="loading" class="upload-demo" drag action="upload" :before-upload="beforeUpload" :http-request="uploadHttpRequest" element-loading-text="正在上传">
            <i class="el-icon-upload" />
            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            <div slot="tip" class="el-upload__tip" />
          </el-upload>
          <span slot="footer" class="dialog-footer" />
        </el-dialog>
      </div>

      <div class="fileplace">
        <el-table :data="tableData" class="el-table" @row-click="findnext" @row-contextmenu="rightClick">
          <el-table-column prop="name" label="文件名" width="500" class="el-table-column" />
          <el-table-column prop="type" label="类型" width="250" />
          <el-table-column prop="size" label="大小" width="200" />
          <el-table-column prop="time" label="上传时间" width="220" />
        </el-table>

        <!-- 右键菜单 -->
        <div id="menu" class="menuDiv">
          <ul class="menuUl">
            <li
              v-for="(item, index) in menus"
              :key="index"
              @click.stop="infoClick(index)"
            >
              {{ item.name }}
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import qs from 'qs'

export default {
  name: 'MainBody',
  data() {
    return {
      input: '',
      tableData: [
        {
          name: '',
          size: '',
          time: '',
          type: ''
        }
      ],
      loading: '',
      path: '',
      prepath: '',
      username: this.$store.getters.username,
      newDialogVisible: false,
      uploadDialogVisible: false,
      menus: [
        { name: '下载', operType: 1 },
        { name: '删除', operType: 2 }
      ],
      tmp: [
        {
          name: '',
          size: '',
          type: ''
        }
      ]
    }
  },
  created() {
    this.getAll()
  },
  methods: {
    getAll() {
      this.tableData = []
      this.path = this.$store.getters.path
      this.username = this.$store.getters.username
      axios.post('/commenUser/getdocFolder',
        qs.stringify({
          pathName: this.path,
          token: this.$store.getters.token
        })).then(res => {
        res.data.docFolders.forEach(item => {
          this.tableData.push({
            name: item.docFileName,
            size: '-',
            time: item.updateTime,
            type: 'folder'
          })
        })
        res.data.documents.forEach(item => {
          this.tableData.push({
            name: item.documentName,
            size: (item.size / 1000).toFixed(2) + 'MB',
            time: item.updateTime,
            type: 'file'
          })
        })
      })
    },
    findnext(row, column, event) {
      const nextpath = this.path + row.name + '/'
      if (row.type === 'folder') {
        this.prepath = this.path
        this.$store.commit('user/SET_PATH', nextpath)
        this.path = this.$store.getters.path
        this.tableData = ''
        axios.post('/commenUser/getdocFolder',
          qs.stringify({
            pathName: nextpath,
            token: this.$store.getters.token
          })).then(res => {
          this.tableData = []
          if (res.data.docFolders.length === 0 && res.data.documents.length === 0) {
            this.$message('文件夹为空！')
          } else {
            res.data.docFolders.forEach(item => {
              this.tableData.push({
                name: item.docFileName,
                size: '-',
                time: item.updateTime,
                type: 'folder'
              })
            })
            res.data.documents.forEach(item => {
              this.tableData.push({
                name: item.documentName,
                size: (item.size / 1000).toFixed(2) + 'MB',
                time: item.updateTime,
                type: 'file'
              })
            })
          }
        })
      }
    },
    goback() {
      console.log(this.prepath)
      this.$store.commit('user/SET_PATH', this.prepath)
      this.getAll()
    },
    newfolder() {
      axios.post('/commenUser/mkdir',
        qs.stringify({
          docFoldername: this.input,
          pathName: this.path,
          token: this.$store.getters.token
        })).then(res => {
        this.tableData = []
        this.getAll()
      })
      this.newDialogVisible = false
    },
    beforeUpload(file) {
      this.loading = true
      const formdata = new FormData()
      formdata.append('File', file)
      formdata.append('pathName', this.path)
      formdata.append('token', this.$store.getters.token)
      axios.post('/commenUser/upload', formdata).then(res => {
        this.tableData = []
        this.getAll()
        this.loading = false
        this.uploadDialogVisible = false
      })
    },
    rightClick(row, column, event) {
      this.tmp.name = row.name
      this.tmp.size = row.size
      this.tmp.type = row.type
      const menu = document.querySelector('#menu')
      event.preventDefault()
      // 根据事件对象中鼠标点击的位置，进行定位
      menu.style.left = event.clientX + 10 + 'px'
      menu.style.top = event.clientY - 30 + 'px'
      // 改变自定义菜单的隐藏与显示
      menu.style.display = 'block'
      menu.style.zIndex = 1000
    },
    infoClick(index) {
      if (index === 0) {
        this.download()
      } else if (index === 1) {
        this.del()
      }
      const menu = document.querySelector('#menu')
      menu.style.display = 'none'
    },
    download() {
      if (this.tmp.type === 'folder') {
        this.$message('无法下载文件夹！')
      } else if (this.tmp.type === 'file') {
        const formdata = new FormData()
        formdata.append('docName', this.tmp.name)
        formdata.append('pathName', this.path)
        formdata.append('token', this.$store.getters.token)
        axios.post('/commenUser/downloadDoc', formdata).then(res => {
          const blob = new Blob([res], { type: 'application/octet-stream' })
          if (window.navigator.msSaveOrOpenBlob) {
            navigator.msSaveBlob(blob, this.tmp.name)// 本地保存
          } else {
            var link = document.createElement('a')
            link.href = window.URL.createObjectURL(blob)
            link.download = this.tmp.name
            link.click()
            window.URL.revokeObjectURL(link.href)
          }
        })
      }
    },
    del() {
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        if (this.tmp.type === 'folder') {
          const formdata = new FormData()
          formdata.append('docFoldername', this.tmp.name)
          formdata.append('pathName', this.path)
          formdata.append('token', this.$store.getters.token)
          axios.post('/commenUser/deldocFolder', formdata).then(res => {
            this.tableData = []
            this.getAll()
          })
        } else {
          const formdata = new FormData()
          formdata.append('filename', this.tmp.name)
          formdata.append('pathName', this.path)
          formdata.append('token', this.$store.getters.token)
          axios.post('/commenUser/deldoc', formdata).then(res => {
            this.tableData = []
            this.getAll()
          })
        }
      })
    }

  }
}
</script>

<style scoped>
.menuDiv {
  display: none;
  position: absolute;

.menuUl {
  height: auto;
  width: auto;
  font-size: 14px;
  text-align: left;
  border-radius: 4px;
  border: none;
  background-color: #ffffff;
  color: #606266;
  list-style: none;
  border: 1px solid #ebeef5;

li {
  width: 140px;
  height: 35px;
  line-height: 35px;
  padding: 0 10px;
  cursor: pointer;
  border-bottom: 1px solid rgba(255, 255, 255, 0.47);

&:hover {
   display: block;
   background-color: #ecf5ff;
   color: #7abbff;
 }
}
}
}

.workplace {
  max-width: 100%;
  background-color: white;
  height: 40px;
  font: 12px/1.5 "Microsoft YaHei", arial, SimSun, "宋体";
  line-height: 30px;
}
.nav{
  max-width: 100%;
  background-color: white;
  height: 20px;
  /*font: 12px/1.5 "Microsoft YaHei", arial, SimSun, "宋体";*/
  font-size: 8px;
  line-height: 20px;
}

.el-table-column {
  max-height: 48px;
  line-height: 48px;
}

.el-table {
  max-width: 100%;
  font: 12px/1.5 "Microsoft YaHei", arial, SimSun, "宋体";
}

#search {
  width: 315px;
  border-radius: 33px;
  background-color: #f7f7f7;
  float: right;
  text-align: center;
  height: 32px;
  line-height: 32px;

}

.search {
  border: none;
  background-color: #f7f7f7;
  height: 30px;
  width: 248px;
}

img {
  width: 30px;
  height: 30px;
}

a {
  color: #424e67;
  text-decoration: none;
}

a:hover {
  color: #09AAFF;
}
.el-icon-delete{
  color:#F56C6C;
}
</style>
