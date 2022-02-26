<template>
  <div>
    <!-- 导航栏 -->
    <div class="navbar">
      <div class="left-text" @click="onClickLeft()">
        <van-icon name="arrow-left" style="position: relative; top: 3px" />返回
      </div>
      <div class="title">预约确认</div>
      <div class="more"></div>
    </div>
    <!-- 收货信息 -->
    <div class="goodsImfo">
      <div class="tit">收货信息</div>

      <ul class="freInfo">
        <li>{{ tableData.contacts }}</li>
        <li>{{ tableData.contactsPhone }}</li>
      </ul>
      <ul style="display: flex; justify-content: space-between">
        <li class="addr">
          <img src="../assets/weizhi.png" class="position" />
          {{ tableData.address }}
        </li>
      </ul>

      <van-divider />
      <ul class="timer">
        <li>上门时间：{{ this.$route.params.time }}</li>
      </ul>
    </div>
    <!-- 箱子信息 -->
    <div class="boxInfo">
      <div class="tit">箱子信息</div>
      <div>
        <ul class="boxImformation">
          <li>{{ this.$route.params.boxStandard }}</li>
          <li>
            <label for style="color: blue">{{
              this.$route.params.boxUnitPrice
            }}</label>
            X {{ this.$route.params.leaseDuration }}
          </li>
        </ul>
        <ul class="boxImformation">
          <li>预约周期</li>
          <li>{{ this.$route.params.leaseDuration }}个月</li>
        </ul>
        <ul class="boxImformation">
          <li>总计积分</li>
          <li>{{ this.$route.params.totalPrice }}</li>
        </ul>
      </div>
    </div>
    <div class="footer">
      <ul>
        <li>实付积分：</li>
        <li style="color: blue">{{ this.$route.params.totalPrice }}</li>
        <li></li>
        <button class="submit" @click="submitSure()">提交</button>
      </ul>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      tableData: {},
      List: [],
    };
  },
  mounted() {
    this.getData();
    // console.log(this.$route.params);
    // this.geBoxData();
  },
  methods: {
    // 获取地址
    getData() {
      this.$axios.get("/app/box/defaultAddress").then((res) => {
        if (200 == res.data.code) {
          this.tableData = res.data.data;

          console.log(this.tableData[0]);
        } else if (500 == res.data.code) {
          this.$toast.fail(res.data.msg);
        }
      });
    },
    onClickLeft() {
      this.$router.go(-1);
    },
    submitSure() {
      var now = new Date();
      var year = now.getFullYear(); //得到年份
      var month = now.getMonth() + 1; //得到月份
      var date = now.getDate(); //得到日期
      var hour = now.getHours(); //得到小时
      var minu = now.getMinutes(); //得到分钟
      var sec = now.getSeconds(); //得到秒
      var time =
        year + "-" + month + "-" + date + " " + hour + ":" + minu + ":" + sec;
      // console.log(time);
      let params = {
        orderName: this.$route.params.orderName,
        emptyBoxOrderTime: time,
        emptyBoxCallName: this.tableData[0].contacts,
        emptyBoxCallPhone: this.tableData[0].contactsPhone,
        emptyBoxCallAddress: this.tableData[0].address,
        emptyBoxCallTime: this.$route.params.time,
        emptyBoxCallInterval: this.$route.params.emptyBoxCallInterval,
        boxStandard: this.$route.params.boxStandard,
        boxUnitPrice: this.$route.params.boxUnitPrice,
        leaseDuration: this.$route.params.leaseDuration,
        totalPrice: this.$route.params.totalPrice,
      };
      console.log(params);
      this.$axios.post("/app/box/order", params).then((res) => {
        if (res.data.code == 200) {
          this.$toast.success(res.data.msg);
        } else if (500 == res.data.code) {
          this.$toast.fail(res.data.msg);
        }
      });
    },
  },
};
</script>
<style scoped>
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
.left-text {
  padding: 10px;
  position: relative;
  bottom: 8px;
}
.more {
  padding: 10px;
}

.tit {
  padding: 10px;
  font-weight: bold;
}
.goodsImfo,
.boxImfo {
  width: 95%;
  height: 150px;
  border-radius: 10px;
  margin: 10px;
  background: white;
}
.tit {
  padding: 10px;
  font-size: 17px;
}
.freInfo {
  font-size: 13px;
  display: flex;
  justify-content: left;
}
li {
  padding-left: 10px;
}
.addr {
  font-size: 10px;
  color: gray;
  margin-top: 10px;
}
.position {
  width: 12px;
  height: 12px;

  position: relative;
  top: 2px;
}

.timer li {
  margin-bottom: 10px;
}
.timer {
  display: flex;
  justify-content: space-between;
  font-size: 15px;
}
.boxInfo {
  padding-right: 10px;
  width: 92%;
  height: 150px;
  background: white;
  border-radius: 10px;
  margin: 10px;
}
.boxImformation {
  padding-top: 0;
  padding-bottom: 10px;
  display: flex;
  justify-content: space-between;
}
.submit {
  border: none;
  border-radius: 8px;
  background: rgb(40, 40, 216);
  width: 80px;
  height: 30px;
  color: white;
  position: relative;
  top: 10px;
  line-height: 30px;
}
.footer {
  background: white;
  position: fixed;
  bottom: 0;
  width: 100%;
  height: 45px;
  line-height: 50px;
}
.footer ul {
  display: flex;
  justify-content: flex-end;
  padding-right: 10px;
}
</style>