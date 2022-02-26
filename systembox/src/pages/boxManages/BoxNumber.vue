<template>
  <div class="user">
    <!-- 查询箱子 -->
    <el-card class="box-card">
      <el-form
        :model="imform"
        ref="imform"
        label-width="100px"
        :rules="rule1"
        :inline="true"
      >
        <el-form-item label="箱子规格" prop="boxStandard" class="searchValue">
          <el-select v-model="imform.boxStandard" placeholder="请选择">
            <el-option
              v-for="item in getSelectBox"
              :key="item.id"
              :label="item.boxStandard"
              :value="item.boxStandard"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="isUsed" class="searchValue">
          <el-select v-model="imform.isUsed" placeholder="请选择">
            <el-option label="未使用" value="0"></el-option>
            <el-option label="已使用" value="1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="使用人" prop="nickName" class="searchValue">
          <el-select v-model="imform.nickName" placeholder="请选择">
            <el-option
              v-for="item in selectUser"
              :key="item.id"
              :label="item.nickName"
              :value="item.nickName"
            ></el-option>
          </el-select>
        </el-form-item>
        <!-- <el-form-item label="状态" prop="searchValue" class="searchValue">
          <el-select v-model="imform.searchValue" placeholder="请选择">
            <el-option
              v-for="item in tableData"
              :key="item.id"
              :label="item.isUsed"
              :value="item.isUsed"
            ></el-option>
          </el-select>
        </el-form-item> -->
      </el-form>
      <el-button type="primary" @click="search()" class="addBtn"
        >查询</el-button
      >
      <el-button @click="resetForm('imform')" class="resetBtn">重置</el-button>
    </el-card>

    <!--箱子列表-->
    <el-card>
      <el-button type="primary" @click="addData()">新增</el-button>
      <el-button type="primary" @click="Delete()">删除</el-button>

      <sirui-table
        :data="tableData"
        :page="page"
        :tableHeight="tableHeight"
        :loading="load"
        @handleSelectionChange="handleSelectionChange"
        @handleSizeChange="handleSizeChange"
        @handleCurrentChange="handleCurrentChange"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column
          v-for="item in columnList"
          :key="item.num"
          :label="item.lable"
          :width="item.width"
          :prop="item.prop"
        ></el-table-column>
      </sirui-table>
    </el-card>

    <!-- <box-manages></box-manages> -->
    <!-- 新增弹出框 -->
    <box-numberAdd ref="addData"></box-numberAdd>
  </div>
</template>
<script>
import SiruiTable from "../../components/SiruiTable/index";
import BoxNumberAdd from "./BoxNumberAdd";
export default {
  components: {
    SiruiTable,
    BoxNumberAdd,
  },
  name: "BoxNumber",
  data() {
    var checkName = (rule, value, callback) => {
      var userName = /^[ a-zA-Z][a-zA-Z0-9]{3,15}$/;
      var email = /^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/;
      var phone = /^1[ 3456789]\d{9}$ /;
      var name = /([\*\“\'<>\/])+/;
      setTimeout(() => {
        if (
          !email.test(value) &&
          !userName.test(value) &&
          !phone.test(value) &&
          name.test(value)
        ) {
          callback(new Error("请输入正确的用户名/客户名/手机号/邮箱"));
        } else {
          callback();
        }
      }, 100);
    };

    return {
      tableHeight: 400,
      load: false,
      dialogVisible: false,
      pwdForm: {
        pass: "",
        checkPass: "",
      },
      imform: {
        isUsed: "",
        nickName: "",
        boxStandard: "",
      },
      rule1: {
        name: {
          validator: checkName,
        },
      },

      columnList: [
        {
          prop: "boxStandard",
          lable: "箱子规格",
          width: 100,
          num: 1,
        },
        {
          prop: "boxCode",
          lable: "箱子编号",
          width: 180,
          num: 2,
        },
        {
          prop: "boxUnitPrice",
          lable: "所需积分",
          width: 180,
          num: 3,
        },
        {
          prop: "isUsed",
          lable: "使用状态",
          width: 200,
          num: 4,
        },
        {
          prop: "usedByName",
          lable: "使用人",
          width: 150,
          num: 5,
        },
        {
          prop: "remark",
          lable: "备注",
          width: 100,
          num: 6,
        },
      ],
      page: {
        pages: 4,
        total: 0,
        pageSize: 10, //每页显示几条
        pageNum: 1, //第几页
      },

      getSelectBox: [],

      selectUser: [],

      tableData: [
        // 	{
        // 	"id": 1, "boxCode": 20200421000001,
        // 	"boxStandard": "30*30*30",
        // 	"boxUnitPrice": 200,
        // 	"usedBy": 1,
        // 	"usedByName": "王东",
        // 	"isUsed": 1,
        // 	"remark": "当前箱子成本比较低",
        // 	"sortNo": 1,
        // 	"createTime": "2018-03-16 11:33:00",
        // 	"createBy": "wangdong",
        // 	"updateTime": "2018-03-16 11:33:00",
        // 	"updateBy": "wangdong",
        // 	"version": 2,
        // 	"delFlag": 0
        // },
        // {
        // 	"id": 2,
        // 	"boxCode": 20200421000002,
        // 	"boxStandard": "40*40*40",
        // 	"boxUnitPrice": 300,
        // 	"usedBy": null,
        // 	"usedByName": null,
        // 	"isUsed": 0,
        // 	"remark": "当前箱子成本低",
        // 	"sortNo": 1,
        // 	"createTime": "2018-03-16 11:33:00",
        // 	"createBy": "wangdong",
        // 	"updateTime": null,
        // 	"updateBy": null,
        // 	"version": 1,
        // 	"delFlag": 0
        // },
      ],
      multipleSelection: [],

      ostatus: "",
    };
  },

  mounted() {
    this.selectBox();
    this.getSelectUser();
    this.getData();
  },

  methods: {
    //转换汉字
    words() {
      for (let i = 0; i < this.tableData.length; i++) {
        if (this.tableData[i].isUsed == 1) {
          this.tableData[i].isUsed = "已使用";
        } else if (this.tableData[i].isUsed == 0) {
          this.tableData[i].isUsed = "未使用";
        }
      }
    },

    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.getData();
    },

    search() {
      this.getData();
    },

    // 查询箱子种类
    selectBox() {
      this.$axios.get("/backend/box/standard/select").then((resp) => {
        console.log(resp);
        if (resp) {
          if (200 == resp.data.code) {
            this.getSelectBox = resp.data.data;
            // this.imform.selectBox = resp.data.data;
            // console.log(this.imform.selectBox);
            this.words();
            this.$message({
              showClose: true,
              message: "查询成功",
              type: "success",
            });
          } else {
            this.$message({
              showClose: true,
              message: "查询失败",
              type: "error",
            });
          }
        }
      });
    },

    // 查询用户
    getSelectUser() {
      this.$axios.get("/backend/customer/list").then((resp) => {
        console.log(resp);
        if (resp) {
          if (200 == resp.data.code) {
            this.selectUser = resp.data.rows;
            this.$message({
              showClose: true,
              message: "查询成功",
              type: "success",
            });
          } else {
            this.$message({
              showClose: true,
              message: "查询失败",
              type: "error",
            });
          }
        }
      });
    },

    //查询数据列表
    getData() {
      this.$axios
        .get(
          `/backend/box/info/list?pageNum=${this.page.pageNum}&pageSize=${this.page.pageSize}&boxStandard=${this.imform.boxStandard}&usedByName=${this.imform.nickName}&isUsed=${this.imform.isUsed}`
        )
        .then((resp) => {
          console.log(resp);
          if (resp) {
            if (200 == resp.data.code) {
              this.tableData = resp.data.rows;
              this.page.total = resp.data.total;
              this.page.pages = Math.round(
                resp.data.total / resp.data.pageSize
              );

              this.words();

              this.$message({
                showClose: true,
                message: "查询成功",
                type: "success",
              });
            } else {
              this.$message({
                showClose: true,
                message: "查询失败",
                type: "error",
              });
            }
          }
        });
    },

    //选中触发事件
    handleSelectionChange(row) {
      this.multipleSelection = row;
      console.log(this.multipleSelection);
    },

    //第几页
    handleCurrentChange(val) {
      this.page.pageNum = val;
      this.getData();
    },

    //抛出每页条数事件
    handleSizeChange(val) {
      this.page.pageSize = val;
      this.getData();
    },

    addData() {
      this.$refs.addData.openDialog(
        true,
        JSON.parse(JSON.stringify(this.multipleSelection))
      );
    },

    //显示新修改弹出窗
    // editData() {
    //   //debugger;
    //   if (1 == this.multipleSelection.length) {
    //     this.$refs.editData.openDialog(
    //       true,
    //       JSON.parse(JSON.stringify(this.multipleSelection))
    //     );
    //   } else if (this.multipleSelection.length > 1) {
    //     this.$message({
    //       type: "info",
    //       message: "请选择一条数据进行操作",
    //     });
    //   } else {
    //     this.$message({ type: "info", message: "请选择数据进行操作" });
    //   }
    // },

    Delete() {
      if (this.multipleSelection.length >= 1) {
        this.$confirm("是否删除", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(() => {
            let params = {
              ids: [],
            };

            for (var i = 0; i < this.multipleSelection.length; i++) {
              params.ids.push(this.multipleSelection[i].id);
            }
            params.ids = params.ids.join(",");
            this.$axios
              .delete("backend/box/info/delete/" + params.ids)
              .then((resp) => {
                if (200 == resp.data.code) {
                  console.log(resp);
                  this.search();
                  this.$message({
                    showClose: true,
                    message: resp.data.msg,
                    type: "success",
                  });
                } else {
                  this.$message({
                    showClose: true,
                    message: resp.data.msg,
                    type: "error",
                  });
                }
              });
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消删除",
            });
          });
      } else {
        this.$message({
          type: "info",
          message: "请选择数据进行操作",
        });
      }
    },
  },
};
</script>
<style scoped>
.user {
  position: absolute;
  width: 1200px;
  height: 800px;
  top: 70px;
  left: 280px;
}
</style>
