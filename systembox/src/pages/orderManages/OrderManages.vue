<template>
  <div class="order">
    <!--查询-->
    <el-card class="box-card">
      <el-form
        :model="orderform"
        ref="orderform"
        label-width="150px"
        :inline="true"
      >
        <el-form-item label="客户名称" prop="nickName" class="name">
          <el-input v-model="orderform.nickName" class="nameInp"></el-input>
        </el-form-item>
        <el-form-item label="订单状态" prop="status" class="status">
          <el-select
            v-model="orderform.status"
            placeholder="请选择"
            class="selected"
          >
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item
          label="空箱预约开始时间"
          prop="emptyBoxOrderTimeStart"
          class="status"
        >
          <el-date-picker
            v-model="orderform.emptyBoxOrderTimeStart"
            type="datetime"
            placeholder="选择日期时间"
            align="right"
            :picker-options="pickerOptions"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item
          label="空箱预约结束时间"
          prop="emptyBoxOrderTimeEnd"
          class="status"
        >
          <el-date-picker
            v-model="orderform.emptyBoxOrderTimeEnd"
            type="datetime"
            placeholder="选择日期时间"
            align="right"
            :picker-options="pickerOptions"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item
          label="重箱预约开始时间"
          prop="heavyBoxOrderTimeStart"
          class="status"
        >
          <el-date-picker
            v-model="orderform.heavyBoxOrderTimeStart"
            type="datetime"
            placeholder="选择日期时间"
            align="right"
            :picker-options="pickerOptions"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item
          label="重箱预约结束时间"
          prop="heavyBoxOrderTimeEnd"
          class="status"
        >
          <el-date-picker
            v-model="orderform.heavyBoxOrderTimeEnd"
            type="datetime"
            placeholder="选择日期时间"
            align="right"
            :picker-options="pickerOptions"
          >
          </el-date-picker>
        </el-form-item>
      </el-form>

      <el-button type="primary" @click="search()" class="addBtn"
        >查询</el-button
      >
      <el-button @click="resetForm('orderform')" class="resetBtn"
        >重置</el-button
      >
    </el-card>
    <!--客户列表-->
    <el-card>
      <el-button type="primary" @click="detail()">详情</el-button>
      <el-button type="primary" @click="Delete()">删除</el-button>
      <sirui-table
        :data="orderData"
        :page="page"
        :tableHeight="tableHeight"
        @handleSizeChange="handleSizeChange"
        @handleSelectionChange="handleSelectionChange"
        @handleCurrentChange="handleCurrentChange"
      >
        <el-table-column type="selection" width="55"> </el-table-column>

        <el-table-column
          label="订单编号"
          prop="orderCode"
          :show-overflow-tooltip="true"
        >
        </el-table-column>
        <el-table-column
          label="订单名称"
          prop="orderName"
          :show-overflow-tooltip="true"
        >
        </el-table-column>
        <el-table-column
          label="订单状态"
          prop="status"
          :show-overflow-tooltip="true"
        >
        </el-table-column>
        <el-table-column
          label="客户名称"
          prop="nickName"
          :show-overflow-tooltip="true"
        >
        </el-table-column>
        <el-table-column
          label="箱子id"
          prop="boxId"
          :show-overflow-tooltip="true"
        >
        </el-table-column>
        <el-table-column
          label="箱子编号"
          prop="boxCode"
          :show-overflow-tooltip="true"
        >
        </el-table-column>
        <el-table-column
          label="箱子规格"
          prop="boxStandard"
          :show-overflow-tooltip="true"
        >
        </el-table-column>
        <el-table-column
          label="所需积分"
          prop="boxUnitPrice"
          :show-overflow-tooltip="true"
        >
        </el-table-column>
        <el-table-column
          label="租赁时长"
          prop="leaseDuration"
          :show-overflow-tooltip="true"
        >
        </el-table-column>
        <el-table-column
          label="实付积分"
          prop="totalPrice"
          :show-overflow-tooltip="true"
        >
        </el-table-column>
        <el-table-column
          label="空箱上门下单时间"
          v-if="emptyShow"
          prop="emptyBoxOrderTime"
          :show-overflow-tooltip="true"
        >
        </el-table-column>
        <el-table-column
          label="重箱上门下单时间"
          prop="heavyBoxOrderTime"
          v-if="heavyShow"
          :show-overflow-tooltip="true"
        >
        </el-table-column>

        <el-table-column
          fixed="right"
          label="操作"
          prop="operation"
          :show-overflow-tooltip="true"
        >
          <template slot-scope="scope">
            <el-button
              size="mini"
              @click="sendNull(scope.row)"
              v-if="scope.row.status === 1"
              style="color: orange"
              >发送空箱</el-button
            >
            <el-button
              size="mini"
              @click="sendNull(scope.row)"
              v-else-if="scope.row.status === 6"
              style="color: black"
              >发送重箱</el-button
            >
            <el-button
              size="mini"
              @click="sendNull(scope.row)"
              v-else-if="scope.row.status === 4"
              style="color: yellow"
              >收取重箱</el-button
            >
            <el-button
              size="mini"
              @click="sendNull(scope.row)"
              v-else-if="scope.row.status === 9"
              style="color: green"
              >收取空箱</el-button
            >
            <el-button
              size="mini"
              v-else-if="scope.row.status === 10"
              style="color: green"
              >已完成</el-button
            >
            <el-button size="mini" v-else style="display: none">--</el-button>
          </template>
        </el-table-column>
      </sirui-table>
      <order-detail ref="details"></order-detail>
    </el-card>
  </div>
</template>

<script>
import SiruiTable from "../../components/SiruiTable/index";
import OrderDetail from "../orderManages/OrderDetail";
export default {
  components: { SiruiTable, OrderDetail },
  data() {
    return {
      pickerOptions: {
        shortcuts: [
          {
            text: "今天",
            onClick(picker) {
              picker.$emit("pick", new Date());
            },
          },
          {
            text: "昨天",
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() - 3600 * 1000 * 24);
              picker.$emit("pick", date);
            },
          },
          {
            text: "一周前",
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit("pick", date);
            },
          },
        ],
      },
      tableHeight: 400,
      emptyShow: true,
      heavyShow: true,
      orderform: {
        nickName: "",
        status: "",
        emptyBoxOrderTimeStart: "",
        emptyBoxOrderTimeEnd: "",
        heavyBoxOrderTimeStart: "",
        heavyBoxOrderTimeEnd: "",
      },
      page: { pages: 0, total: 0, pageSize: 10, pageNum: 1 },
      options: [
        {
          label: "待发空箱",
          value: 1,
        },
        {
          label: "已发空箱",
          value: 2,
        },
        {
          label: "送达空箱",
          value: 3,
        },
        {
          label: "待收重箱",
          value: 4,
        },
        {
          label: "已存储",
          value: 5,
        },
        { label: "待发重箱", value: 6 },
        {
          label: "已发重箱",
          value: 7,
        },
        {
          label: "送达重箱",
          value: 8,
        },
        {
          label: "待收空箱",
          value: 9,
        },
        {
          label: "已完成",
          value: 10,
        },
      ],
      allTableData: [],
      orderData: [],
      multipleSelection: [],
      operate: "",
    };
  },
  mounted() {
    this.search();
    this.heavyBox();
  },
  methods: {
    heavyBox(data) {
      console.log(data);
    },
    resetForm(formName) {
      if (this.$refs[formName] != undefined) {
        this.$refs[formName].resetFields();
        this.search();
      }
    },

    search() {
      this.$axios
        .get(this.$store.state.url + "/backend/order/list", {
          params: {
            pageNum: this.page.pageNum,
            pageSize: this.page.pageSize,
            status: this.orderform.status,
            nickName: this.orderform.nickName,
            emptyBoxOrderTimeEnd: this.orderform.emptyBoxOrderTimeEnd,
            emptyBoxOrderTimeStart: this.orderform.emptyBoxOrderTimeStart,
            heavyBoxOrderTimeStart: this.orderform.heavyBoxOrderTimeStart,
            heavyBoxOrderTimeEnd: this.orderform.heavyBoxOrderTimeEnd,
          },
        })
        .then((resp) => {
          console.log(resp);
          if (200 == resp.data.code) {
            this.orderData = resp.data.rows;
            this.page.total = resp.data.total;
            this.page.pages = Math.round(resp.data.total / resp.data.pageSize);
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
        });
    },

    // initsearch() {
    //   this.$axios
    //     .get(
    //       `/backend/order/list?pageNum=${this.page.pageNum}&pageSize=${this.page.pageSize}`
    //     )
    //     .then((resp) => {
    //       console.log(resp);
    //       this.orderData = resp.data.rows;
    //       //this.orderform.status = resp.data.rows;
    //       this.operate = resp.data.rows[0].status + "";
    //       console.log(this.operate);
    //       // console.log(this.orderData);
    //       // console.log(this.orderform);
    //       //   .filter((item) => {
    //       //     //console.log(typeof item.status);
    //       //     return (
    //       //       item.nickName.includes(this.orderform.nickName) &&
    //       //       (item.status + "").includes(this.orderform.status)
    //       //     );
    //       //   })
    //       //   .slice(
    //       //     (this.page.pageNum - 1) * this.page.pageSize,
    //       //     this.page.pageNum * this.page.pageSize
    //       //   );
    //       // this.page.total = this.allTableData.length;
    //     });
    // },

    // search() {
    // this.orderData = this.allTableData;
    // .filter((item) => {
    //   return (
    //     item.nickName.includes(this.orderform.nickName) &&
    //     item.status.includes(this.orderform.status)
    //   );
    // })
    //   .slice(
    //     (this.page.pageNum - 1) * this.page.pageSize,
    //     this.page.pageNum * this.page.pageSize
    //   );
    // this.page.total = this.allTableData.length;
    // },

    deleteOrder(item) {
      if (!this.multipleSelection.length) {
        this.$message({
          type: "error",
          message: "请勾选数据",
        });
        return;
      }

      this.$confirm("确认要删除选中的数据吗？")
        .then(() => {
          let currentIndex = this.allTableData.fildIndex((myitem) => {
            return myitem.boxId === item.boxId;
          });

          this.allTableData.splice(currentIndex, 1);

          // this.search();
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },

    detail() {
      if (1 == this.multipleSelection.length) {
        this.$refs.details.openDialog(
          true,
          JSON.parse(JSON.stringify(this.multipleSelection))
        );
      } else if (this.multipleSelection.length < 1) {
        this.$message({
          type: "info",
          message: "请选择一条数据进行操作",
        });
      } else {
        this.$message({
          type: "info",
          message: "请选择数据进行操作",
        });
      }
    },

    ZX() {
      this.$refs.heavyBox.openDialog(true);
    },

    handleSizeChange(val) {
      this.page.pageSize = val;
      this.search();
    },

    handleSelectionChange(row) {
      this.multipleSelection = row;
    },

    handleCurrentChange(val) {
      this.page.pageNum = val;
      this.search();
    },

    sendNull(row) {
      if (row.status == 1) {
        this.$axios
          .put(
            `http://localhost:8081/backend/order/operate/${row.id}/2/${row.version}`,
            { params: {} }
          )
          .then((resp) => {
            if (200 == resp.data.code) {
              console.log(resp);
              if (resp) {
                this.search();
                this.$message({
                  showClose: true,
                  message: "发送空箱成功",
                  type: "success",
                });
              }
            } else {
              this.$message({
                showClose: true,
                message: "发送空箱失败",
                type: "error",
              });
            }
          });
      } else if (row.status == 4) {
        this.$axios
          .put(
            `http://localhost:8081/backend/order/operate/${row.id}/5/${row.version}`,
            { params: {} }
          )
          .then((resp) => {
            if (200 == resp.data.code) {
              console.log(resp);
              if (resp) {
                this.search();
                this.$message({
                  showClose: true,
                  message: "收取重箱成功",
                  type: "success",
                });
              }
            } else {
              this.$message({
                showClose: true,
                message: "收取重箱失败",
                type: "error",
              });
            }
          });
      } else if (row.status == 6) {
        this.$axios
          .put(
            `http://localhost:8081/backend/order/operate/${row.id}/7/${row.version}`,
            { params: {} }
          )
          .then((resp) => {
            if (200 == resp.data.code) {
              console.log(resp);
              if (resp) {
                this.search();
                this.$message({
                  showClose: true,
                  message: "发送重箱成功",
                  type: "success",
                });
              }
            } else {
              this.$message({
                showClose: true,
                message: "发送重箱失败",
                type: "error",
              });
            }
          });
      } else if (row.status == 9) {
        this.$axios
          .put(
            `http://localhost:8081/backend/order/operate/${row.id}/10/${row.version}`,
            { params: {} }
          )
          .then((resp) => {
            if (200 == resp.data.code) {
              console.log(resp);
              if (resp) {
                this.search();
                this.$message({
                  showClose: true,
                  message: "收取空箱成功",
                  type: "success",
                });
              }
            } else {
              this.$message({
                showClose: true,
                message: "收取空箱失败",
                type: "error",
              });
            }
          });
      }
    },

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
              .delete("backend/order/delete/" + params.ids)
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
.order {
  position: absolute;
  width: 1200px;
  height: 800px;
  top: 70px;
  left: 280px;
}

.box-card {
  height: 250px;
  margin-bottom: 10px;
}

.name {
  float: left;
  position: relative;
  top: 15px;
  left: 0px;
  margin-right: 40px;
}

.nameInp {
  width: 220px;
}

.status {
  width: 400px;
  float: left;
  position: relative;
  top: 15px;
  left: 0px;
}

.addBtn,
.resetBtn {
  float: left;
  position: relative;
  top: 15px;
  margin-right: 20px;
}
.data {
  float: left;
  position: relative;
  top: 0px;
  left: 0px;
  top: 10px;
}
</style>
