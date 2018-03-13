/**
 * Created by liwei on 2017/9/27.
 */
var async = require('asyncawait/async');
var await = require('asyncawait/await');

module.exports = {

  /**
   * time毫秒后执行
   * @param time
   * @returns {Promise}
   */
  sleep: function (time) {

    return new Promise(function (resolve, reject) {

      setTimeout(function () {
        resolve();
      }, time);

    });

  },

  /**
   * 生成num位的数字验证码
   * @param num 生成的位数
   * @returns {string}
   */
  generateVeriCode: function (num) {
    var str = '';

    for (var i = 0; i < num; i++) {
      str += Math.floor(Math.random() * 10);
    }

    return str;
  },

  /**
   * 通过通用编码获取配置列表
   */
  getConfigListByCode: async(function(code) {
    var result = await(Xt_common_type.findOne({
      where : {code: code},
      select: ['id', 'code', 'typename']
    }).populate("config_items"));

    return result;
  }),

  /**
   * 根据餐厅ID判断餐厅是否营业
   * 没有设置的话默认营业中
   * 返回true / false
   */
  restaurantIfOpen: async(function (id) {
    if (!id) {
      return;
    }

    // 当前时间是否在营业时间范围内判断  开始
    var time_open = true;
    var startDate = new Date();
    var endDate = new Date();
    var now = new Date().getTime();

    var time = await(Business_hours.find({restaurant_id: id}));
    if (time && time.length > 0) {
      startDate.setHours(time[0].start.split(":")[0]);
      startDate.setMinutes(time[0].start.split(":")[1]);

      endDate.setHours(time[0].end.split(":")[0]);
      endDate.setMinutes(time[0].end.split(":")[1]);
      if (now - startDate.getTime() < 0 || now - endDate > 0) {
        time_open = false;
      }
    }
    // 当前时间是否在营业时间范围内判断 结束

    return time_open;
  }),

  mul: function (arg) {
    var m = 0, num = 1;

    for (var i = 0; i < arg.length; i ++) {
      try { m += arg[i].toString().split('.')[1].length }catch (e){}
    }
    for (var i = 0; i < arg.length; i ++) {
      num = Number(num.toString().replace('.', '')) * Number(arg[i].toString().replace('.', ''))
    }

    return num / Math.pow(10, m);
  }
};