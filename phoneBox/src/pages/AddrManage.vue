<template>
  <div class="addrmanage">
    <!-- 导航栏 -->
    <div class="navbar">
      <div class="left-text" @click="onClickLeft()">
        <van-icon name="arrow-left" style="position: relative; top: 3px" />返回
      </div>

      <div class="title">地址管理</div>
      <div class="more"></div>
    </div>
    <div class="addrDetail">
      <van-pull-refresh v-model="refreshing" @refresh="onRefresh">
        <van-list
          v-model="loading"
          :immediate-check="false"
          :finished="finished"
          finished-text="- 没有更多了 -"
          @load="onLoad"
          :offset="50"
        >
          <van-address-list
            v-model="chosenAddressId"
            :list="list"
            default-tag-text="默认"
            @edit="onEdit"
            @add="onAdd"
            @select="onSelect"
          />
        </van-list>
      </van-pull-refresh>
    </div>
    <div class="Btn">
      <!-- <van-button type="danger" @click="onAdd()" class="add">新增地址</van-button> -->

      <van-button
        type="danger"
        @click="addrDelete()"
        class="delete"
        v-show="isShow"
        >删除</van-button
      >

      <van-button type="danger" @click="onAdd()" class="add"
        >新增地址</van-button
      >
    </div>
  </div>
</template>
<script>
import $ from "jquery";
import areaList from "../assets/js/area";
export default {
  data() {
    return {
      refreshing: false, //下拉加载开关 ture 进行加载  flas停止加载
      finished: false, // 数据是否请求结束，结束会先显示- 没有更多了 -
      count: 0,
      isShow: false,
      chosenAddressId: "1",
      list: [],
      id: "",
      pageNum: 0,
      pageSize: 5,
      total: 0,
      isLoading: false,
      loading: false, //是否转圈圈,
      temporary: [],
    };
  },
  mounted() {
    this.onLoad();
  },
  methods: {
    onClickLeft() {
      this.$router.go(-1);
    },
    getData() {
      var arr = {};
      $.ajax({
        url:
          this.$store.state.url +
          "/app/address/list?" +
          "pageNum=" +
          this.pageNum +
          "&pageSize=" +
          this.pageSize, //url路径
        type: "GET", //GET
        // url:'',//url路径
        // type:'POST', //GET
        async: false, //或false,是否异步
        // data:{//参数

        // },
        headers: {
          Authorization: " Bearer " + localStorage.getItem("token"),
        },
        timeout: 5000, //超时时间
        dataType: "json",
        beforeSend: function (xhr) {},
        success: function (data, textStatus, jqXHR) {
          if (200 == data.code) {
            // console.log(data);
            arr.data = data.rows;
            arr.total = data.total;
            // this.total = data.total;
            // console.log(arr.data);
          }
        },
        error: function (xhr, textStatus) {
          console.log("获取失败");
        },
        complete: function () {},
      });

      return arr;

      // this.$axios
      //   .get(
      //     "prod-api/app/address/list?" +
      //       "pageNum=" +
      //       this.pageNum +
      //       "&pageSize=" +
      //       this.pageSize
      //   )
      //   .then(res => {
      //     if (200 == res.data.code) {
      //       this.finished = false;
      //       this.list = res.data.rows;
      //       this.total = res.data.total;

      //       if (this.list != "") {
      //         this.isShow = true;
      //       }
      //       for (var i = 0; i < res.data.rows.length; i++) {
      //         this.list[i].name = res.data.rows[i].contacts;
      //         this.list[i].tel = res.data.rows[i].contactsPhone;
      //         this.list[i].address = res.data.rows[i].address;

      //   if (res.data.rows[i].isDefault == 0) {
      //     this.list[i].isDefault = true;
      //   } else if (res.data.rows[i].isDefault == 1) {
      //     this.list[i].isDefault = false;
      //   }
      // }

      //       // console.log(res.data.rows.contacts);
      //     } else if (res.data.code == 500) {
      //       this.$toast.fail(res.data.msg);
      //     }
      //     // console.log(this.list);
      //   });
    },
    onSelect(row) {
      this.id = row.id;
      row.isDefault = 0;
      // console.log(row);
      let params = {
        id: row.id,
        contacts: row.name,
        contactsPhone: row.tel,
        address: row.address,
        isDefault: row.isDefault,
      };
      this.$axios.put("/app/address/update", params).then((res) => {
        if (200 == res.data.code) {
          this.$toast.success(res.data.msg);
          this.onRefresh();
          // this.list = [];
          // this.list = this.getData().data;
        } else if (500 == res.data.code) {
          this.$toast.fail(res.data.msg);
        }
      });
    },
    onLoad() {
      setTimeout(() => {
        if (this.refreshing) {
          this.list = [];
          this.pageNum = 0;
          this.refreshing = false;
        }
        // debugger;
        this.pageNum++;
        console.log(this.pageNum);
        console.log("getDATA");
        console.log(this.getData());

        this.temporary = this.getData().data;
        this.total = this.getData().total;
        console.log("temporary");

        console.log(this.temporary);

        // console.log(this.total);

        for (var i = 0; i < this.temporary.length; i++) {
          this.temporary[i].name = this.temporary[i].contacts;
          this.temporary[i].tel = this.temporary[i].contactsPhone;
          this.temporary[i].address = this.temporary[i].address;

          if (this.temporary[i].isDefault == 0) {
            this.temporary[i].isDefault = true;
          } else if (this.temporary[i].isDefault == 1) {
            this.temporary[i].isDefault = false;
          }
          this.list.push(this.temporary[i]);
          if (this.list != "") {
            this.isShow = true;
          }
        }

        this.loading = false;

        if (this.list.length >= this.total) {
          this.finished = true;
        }
      }, 1000);
    },
    onRefresh() {
      // 清空列表数据
      this.finished = false;
      this.refreshing = true;
      // 重新加载数据
      // 将 loading 设置为 true，表示处于加载状态
      this.loading = true;
      this.onLoad();
    },

    // 删除地址
    addrDelete() {
      this.$axios.delete("/app/address/delete/" + this.id).then((res) => {
        if (200 == res.data.code) {
          this.getData();
          this.$toast.success(res.data.msg);
        } else if (res.data.code == 500) {
          this.$toast.fail(res.data.msg);
        }
      });
    },

    onAdd() {
      this.$router.push("/addradd");
    },
    onEdit(item, id) {
      // console.log(item + "hh " + id);
      this.$router.push({
        name: "addredit",
        query: { id: this.list[id].id },
      });
    },
  },
};
</script>
<style scoped lang="scss">
.navbar {
  width: 100%;
  height: 46px;
  background: url("../assets/bg2.png");
  position: fixed;
  display: flex;
  justify-content: space-between;
  color: white;
  z-index: 1;
  font-size: 15px;
  line-height: 46px;
  position: relative;
  top: 0;
}
.title {
  // padding: 10px;
  padding-right: 30px;
}
.left-text {
  padding: 10px;
  position: relative;
  bottom: 8px;
}
.more {
  padding: 10px;
}
/deep/ .van-button--danger {
  color: #fff;
  background-color: #1989fa;
  border: 1px solid #1989fa;
}

.submit {
  position: relative;
  top: 200px;
  left: 50px;
  width: 250px;
  height: 40px;
  line-height: 40px;
  background: url("../assets/bg2.png");

  color: white;
  border: none;
  border-radius: 10px;
}
.van-button {
  width: 300px;
  position: absolute;
  bottom: 80px;
  left: 30px;
}
.delete {
  position: fixed;
  // top: 5px;
  border-radius: 50px;
  width: 300px;
  height: 40px;
  // left: 30px;
  background: rgb(204, 31, 31);
  border: 0;
  bottom: 80px;
}
.Btn {
  position: relative;

  width: 100%;
  height: 200px;
}
.add {
  position: fixed;
  bottom: 130px;

  border-radius: 50px;
  width: 300px;
  height: 40px;
  // left: 30px;

  border: 0;
}
.addrDetail {
  height: 400px;
  overflow: scroll;
}
/deep/ .van-address-list__add {
  visibility: hidden;
}
</style>
