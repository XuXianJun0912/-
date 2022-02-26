<template>
  <div class="nullBox">
    <!-- 导航栏 -->
    <div class="navbar">
      <div class="left-text" @click="ClickLeft()">
        <van-icon name="arrow-left" />
      </div>
      <div class="title">空箱预约</div>
      <div class="more"></div>
    </div>
    <!-- 收货信息 -->
    <div class="goodsImfo">
      <div class="tit">收货信息</div>
      <div>
        <!-- 默认地址为空显示 -->
        <div v-if="tableData==undefined">
          <ul class="freInfo">
            <li></li>
            <li></li>
            <li></li>
          </ul>
          <ul style=" display: flex; justify-content: space-between;">
            <li class="addr"></li>
            <li class="toUpdate" @click="addrEdit">
              <van-icon name="arrow" />
            </li>
          </ul>
        </div>

        <!-- 默认地址不为空显示 -->
        <div v-else>
          <ul class="freInfo">
            <li>{{ tableData.contacts }}</li>
            <li>{{ tableData.contactsPhone }}</li>
            <li v-show="Init">
              <van-tag type="primary">默认</van-tag>
            </li>
          </ul>
          <ul style=" display: flex; justify-content: space-between;">
            <li class="addr">
              <img src="../assets/weizhi.png" class="position" />
              {{ tableData.address }}
            </li>
            <li class="toUpdate" @click="addrEdit">
              <van-icon name="arrow" />
            </li>
          </ul>
        </div>
        <van-divider />
        <ul class="orderName">
          <li>订单名称：</li>
          <li>
            <van-field v-model="orderName" placeholder="请输入订单名称" />
          </li>
        </ul>

        <ul class="timer">
          <li>上门时间：&#8195;{{ currentDate}}</li>
          <li class="toUpdate" @click="homeTime">
            <van-icon name="arrow" />
          </li>
        </ul>

        <ul class="timer">
          <li>上门时段：&#8195;{{emptyBoxCallInterval}}</li>
          <li class="toUpdate" @click="homePeriod">
            <van-icon name="arrow" />
          </li>
        </ul>
      </div>
    </div>

    <!-- 箱子信息 -->
    <div class="boxImfo">
      <div class="tit">箱子信息</div>
      <ul class="boxChoseT">
        <li>所需积分</li>
        <li>
          <div class="standardT">规格</div>
        </li>
        <li class="toUpdate" @click="minBox"></li>
      </ul>
      <div v-for="(item,index) in boxList" :key="index">
        <van-radio-group v-model="radio">
          <ul class="boxChose">
            <li>
              <van-radio :name="item.id">{{item.boxUnitPrice}}</van-radio>
            </li>
            <li>
              <div class="standard">{{item.boxStandard}}</div>
            </li>
            <li class="toUpdate" @click="minBox">
              <!-- <van-icon name="arrow" /> -->
            </li>
          </ul>

          <van-divider />
        </van-radio-group>
      </div>
      <div class="cycle">
        <ul class="cycleStyle">
          <li style="margin-left:10px;margin-bottom:15px;position:relative;bottom:5px">预约周期</li>
          <li class="monthNum">
            <van-stepper v-model="cycle" />
            <label class="monthWord">月</label>
          </li>
        </ul>
      </div>
    </div>
    <button class="submit" @click="submitSure()">提交</button>

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
      orderName: "",
      boxList: [],
      sdshow: false,
      currentStartTime: "",
      currentEndTime: "",
      minDate: new Date(),
      maxDate: new Date(2025, 10, 1),
      currentDate: "",
      cycle: "",
      radio: "",
      timeValue: "",
      show: false, //弹出层是否显示
      tableData: [],
      emptyBoxCallInterval: "",
      boxStandard: "", //箱子规格,
      boxUnitPrice: "" //箱子积分
    };
  },
  mounted() {
    this.getData();
    this.geBoxData();
  },
  methods: {
    getData() {
      this.$axios.get("/app/box/defaultAddress").then(res => {
        if (200 == res.data.code) {
          console.log(res.data.data);
          this.tableData = res.data.data[0];
          this.Init = true;

          // console.log(this.tableData);
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
      this.emptyBoxCallInterval =
        this.currentStartTime + "-" + this.currentEndTime;
    },
    // 点击确认 结束时段格式

    confirmIntervalEnd(value) {
      // console.log(value);
      this.currentEndTime = value;
      this.emptyBoxCallInterval =
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
      // console.log(y);
      // console.log(m);
      // console.log(d);
      // console.log(h);
      // console.log(min);
      // console.log(this.currentDate);
    },
    // 点击取消
    cancelPicker() {
      this.show = false;
      this.sdshow = false;
    },
    ClickLeft() {
      this.$router.go(-1);
    },
    homePeriod() {
      this.sdshow = true;
    },
    homeTime() {
      this.show = true;
    },
    minBox() {},
    // 默认地址跳转
    addrEdit() {
      this.$router.push("/addrmanage");
    },

    submitSure() {
      // console.log(this.cycle);
      // console.log("时间" + this.currentDate);
      for (var i = 0; i < this.boxList.length; i++) {
        if (this.radio == this.boxList[i].id) {
          this.boxStandard = this.boxList[i].boxStandard;
          this.boxUnitPrice = this.boxList[i].boxUnitPrice;
        }
      }
      if (
        this.orderName != "" &&
        this.totalPrice != "" &&
        this.emptyBoxCallInterval != "" &&
        this.boxStandard != "" &&
        this.currentDate != ""
      ) {
        this.$router.push({
          name: "appointment",
          params: {
            time: this.currentDate,
            boxStandard: this.boxStandard,
            boxUnitPrice: this.boxUnitPrice,
            leaseDuration: this.cycle,
            totalPrice: this.cycle * this.boxUnitPrice,
            boxId: this.radio,
            emptyBoxCallInterval: this.emptyBoxCallInterval,
            orderName: this.orderName
          }
        });
      } else {
        if (this.orderName == "") {
          this.$toast.fail("订单名称不能为空！");
        } else if (this.totalPrice == "") {
          this.$toast.fail("实付积分不能为空！");
        } else if (this.emptyBoxCallInterval == "") {
          this.$toast.fail("上门时段不能为空！");
        } else if (this.boxStandard == "") {
          this.$toast.fail("规格不能为空！");
        } else if (this.currentDate == "") {
          this.$toast.fail("上门时间不能为空！");
        }
      }
    },
    //获取箱子规格信息
    geBoxData() {
      this.$axios.get("/app/box/standard/select").then(res => {
        if (200 == res.data.code) {
          this.$toast.success(res.data.msg);
          this.boxList = res.data.data;
        }
      });
    }
  }
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
.left-text {
  padding: 10px;
  position: relative;
  bottom: 8px;
}
.more {
  padding: 10px;
}
.goodsImfo {
  width: 95%;
  height: 260px;
  border-radius: 10px;
  margin: 10px;
  background: white;
}
.boxImfo {
  border-radius: 10px;
  margin: 10px;
  width: 95%;
  background: white;
  min-height: 200px;
  height: auto !important;
}
.tit {
  font-weight: bold;
  padding: 10px;
  font-size: 16px;
}

.freInfo {
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
  margin-left: 0px;
  position: relative;
  top: 2px;
}
.toUpdate {
  color: gray;
  font-size: 20px;
  padding-right: 20px;
}
.timer li {
  margin-bottom: 10px;
}
.timer,
.boxChose,
.cycleStyle {
  display: flex;
  justify-content: space-between;
  font-size: 15px;
}
.orderName {
  display: flex;
  justify-content: space-between;
  font-size: 15px;
}
.boxChoseT {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
  margin-top: 10px;

  font-size: 15px;
  font-weight: bold;
  color: rgb(80, 154, 219);
}
.standard {
  font-size: 13px;
  position: relative;
}
.monthNum {
  position: relative;
  top: -5px;
  right: 50px;
}

.monthWord {
  position: absolute;
  bottom: 13px;
  left: 120px;
}
.submit {
  border: none;
  border-radius: 50px;
  background: url("../assets/bg2.png");
  width: 300px;
  height: 40px;
  color: white;
  position: relative;
  left: 30px;
  top: 20px;
}
.van-field {
  margin-right: 100px;
  padding: 0;
  padding-bottom: 10px;
}
/deep/.van-tag--primary {
  width: 20px;
}
</style>
>