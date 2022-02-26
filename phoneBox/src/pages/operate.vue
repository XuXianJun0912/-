<template>
  <div class="allList" id="allList">
    <!-- 导航栏 -->
    <div class="navbar">
      <div class="left-text" @click="onClickLeft()">
        <van-icon name="arrow-left" style="position: relative; top: 3px" />返回
      </div>
      <div class="title">操作</div>
      <div class="more"></div>
    </div>

    <div class="imform">
      <van-list>
        <div>
          <ul class="list">
            <li style="font-size: 16px; font-weight: bold; padding: 10px">
              收货信息
            </li>
          </ul>
          <ul class="list">
            <div>
              <!-- 默认地址为空显示 -->
              <div v-if="tableData == undefined">
                <ul class="freInfo">
                  <li></li>
                  <li></li>
                  <li></li>
                </ul>
                <ul style="display: flex; justify-content: space-between">
                  <li class="addr"></li>
                  <li class="toUpdate" @click="addrEdit">
                    <van-icon name="arrow" />
                  </li>
                </ul>
              </div>

              <!-- 默认地址不为空显示 -->
              <div>
                <ul class="freInfo">
                  <li>{{ this.AddrtableData.contacts }}&#8195;</li>
                  <li>&#8195;{{ this.AddrtableData.contactsPhone }}</li>
                  <li v-show="Init">
                    &#8195;
                    <van-tag type="primary">&#160;默认</van-tag>
                  </li>
                </ul>
                <ul style="display: flex; justify-content: space-between">
                  <li class="addr">
                    <img src="../assets/weizhi.png" class="position" />
                    {{ this.AddrtableData.address }}
                  </li>
                  <li class="toUpdate" @click="addrEdit">
                    <van-icon name="arrow" />
                  </li>
                </ul>
              </div>
            </div>
          </ul>
        </div>
      </van-list>
    </div>
    <div>
      <div class="boxImfo">
        <div style="font-size: 16px; font-weight: bold; padding: 10px">
          箱子信息
        </div>

        <ul class="list">
          <li
            style="font-size: 15px; padding: 12px; color: gray"
            @click="homeTime"
          >
            重箱提取时间：{{ currentDate }}
          </li>

          <van-divider />
          <li
            style="font-size: 15px; padding: 12px; color: gray"
            @click="homePeriod"
          >
            重箱提取时段：{{ heavyBoxCallInterval }}
          </li>
        </ul>
      </div>
      <van-button
        type="info"
        @click="orderOperate"
        style="
          border-radius: 50px;
          font-size: 20px;
          line-height: 40px;
          width: 300px;
          height: 40px;
          margin: 10px;
          positon: relative;
          top: 120px;
          left: 20px;
        "
        >提 交</van-button
      >
    </div>

    <van-popup v-model="show" position="bottom" :style="{ height: '50%' }">
      <!-- 上门时间 -->
      <van-datetime-picker
        @cancel="cancelPicker"
        @confirm="confirmPicker"
        v-model="timeValue"
        type="date"
        title="选择完整时间"
        :min-date="minDate"
        :max-date="maxDate"
      />
    </van-popup>
    <van-popup v-model="sdshow" position="bottom" :style="{ height: '50%' }">
      <!-- 上门时段 -->
      <van-datetime-picker
        @cancel="cancelPicker"
        @confirm="confirmIntervalStart"
        v-model="currentStartTime"
        type="time"
        title="选择开始时间"
        :min-hour="10"
        :max-hour="20"
      />
      <van-datetime-picker
        @cancel="cancelPicker"
        @confirm="confirmIntervalEnd"
        v-model="currentEndTime"
        type="time"
        title="选择结束时间"
        :min-hour="10"
        :max-hour="20"
      />
    </van-popup>
  </div>
</template>
<script>
export default {
  data() {
    return {
      Init: false,
      tableData: [],
      boxData: [],
      AddrtableData: {},
      operate: "",
      heavyBoxOrderTime: "",
      rows: "",
      minDate: new Date(),
      maxDate: new Date(2025, 10, 1),
      sdshow: false,
      show: false,
      currentStartTime: "",
      currentEndTime: "",
      currentDate: "",
      heavyBoxCallInterval: "",
      timeValue: "",
    };
  },
  mounted() {
    // 调数据接口
    this.getData();
    this.getAddrData();
  },
  methods: {
    // 默认地址
    getAddrData() {
      this.$axios.get("/app/box/defaultAddress").then((res) => {
        if (200 == res.data.code) {
          console.log(res.data.data[0]);
          this.AddrtableData = res.data.data[0];
          this.Init = true;

          console.log(this.AddrtableData);
        } else if (500 == res.data.code) {
          this.$toast.fail(res.data.msg);
        }
      });
    },
    // 点击确认 开始时段格式

    confirmIntervalStart(value) {
      // console.log(value);
      this.currentStartTime = value;
      this.sdshow = false;
      // console.log(this.emptyBoxCallInterval);
      this.heavyBoxCallInterval =
        this.currentStartTime + "-" + this.currentEndTime;
    },
    // 点击确认 结束时段格式

    confirmIntervalEnd(value) {
      // console.log(value);
      this.currentEndTime = value;
      this.heavyBoxCallInterval =
        this.currentStartTime + "-" + this.currentEndTime;
      this.sdshow = false;
      // console.log(this.emptyBoxCallInterval);
    },
    // 点击确认 日期格式
    confirmPicker(value) {
      // console.log(value);
      let y = value.getFullYear();
      let m = value.getMonth() + 1;
      let d = value.getDate();
      let h = value.getHours();
      let min = value.getMinutes();
      let s = value.getSeconds();
      this.currentDate = y + "-" + m + "-" + d;
      this.show = false;
    },
    // 点击取消
    cancelPicker() {
      this.show = false;
      this.sdshow = false;
    },

    // 订单操作
    orderOperate() {
      this.heavyBoxOrderTime =
        new Date().getFullYear() +
        "-" +
        new Date().getMonth() +
        "-" +
        new Date().getDate() +
        " " +
        new Date().getHours() +
        ":" +
        new Date().getMinutes() +
        ":" +
        new Date().getSeconds();
      // console.log(this.operate);
      var allInfo = {
        heavyBoxOrderTime: this.heavyBoxOrderTime,
        heavyBoxCallName: this.AddrtableData.contacts,
        heavyBoxCallPhone: this.AddrtableData.contactsPhone,
        heavyBoxCallAddress: this.AddrtableData.address,
        heavyBoxCallTime: this.currentDate,
        heavyBoxCallInterval: this.heavyBoxCallInterval,
      };
      this.operate = parseInt(this.$route.params.status) + 1;
      console.log(this.$route.params.status);
      console.log(this.operate);
      this.$axios
        .put(
          "/app/order/operate/" +
            this.$route.params.id +
            "/" +
            this.operate +
            "/" +
            this.$route.params.version,
          allInfo
        )
        .then((res) => {
          if (res.data.code == 200) {
            this.$toast.success(res.data.msg);
            this.$router.push("/orderList");
          } else if (res.data.code == 500) {
            this.$toast.fail(res.data.msg);
          }
        });
    },
    // 默认地址跳转
    addrEdit() {
      this.$router.push("/addrmanage");
    },
    homePeriod() {
      this.sdshow = true;
    },
    homeTime() {
      this.show = true;
    },
    onClickLeft() {
      this.$router.go(-1); //返回上一层
    },
    getData() {
      this.$axios.get("/app/order/info/" + this.$route.params.id).then((res) => {
        if (res.data.code == 200) {
          this.$toast.success(res.data.msg);
          this.rows = res.data.data;
          console.log(this.rows);
        }
      });
    },
  },
};
</script>
<style scoped>
.imform {
  width: 95%;
  height: 200px;
  position: relative;
  top: 80px;
  margin: 10px;
  background: white;
  border-radius: 10px;
}
.boxImfo {
  width: 95%;
  height: 200px;
  position: relative;
  top: 100px;
  margin: 10px;
  background: white;
  border-radius: 10px;
}
.navbar {
  width: 100%;
  height: 46px;
  background: url("../assets/bg2.png");
  position: fixed;
  top: 0;
  display: flex;
  justify-content: space-between;
  color: white;
  z-index: 1;
  font-size: 15px;
  line-height: 46px;
}
.title {
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
.list {
  padding: 10px;
}

.boxlist {
  border-top: 1px solid rgb(223, 221, 221);
}
.position {
  width: 12px;
  height: 12px;
  margin-left: 0px;
  position: relative;
  top: 2px;
}
.toUpdate {
  color: gray;
  font-size: 20px;
  padding-right: 20px;
}
.freInfo {
  display: flex;
  justify-content: left;
  padding: 10px;
}
.van-tag--primary {
  height: 50px;
  width: 30px;
  text-align: center;
}
</style>