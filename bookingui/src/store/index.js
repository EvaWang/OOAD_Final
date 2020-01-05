import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    userInfo: { token: "", username: "" },
    searchCondition: {},
    order: {},
    checkoutList: {},
    checkoutLen: 0
  },
  getters: {
    getSearchCondition: state => {
      // {"locality":null,"stars":[null,null,null,true],"roomType":1,"startDate":"2020-01-09","endDate":"2020-01-15"}
      if (!state.searchCondition.locality && 
        !state.searchCondition.stars && 
        !state.searchCondition.roomType && 
        !state.searchCondition.endDate && 
        !state.searchCondition.startDate) {
          
        var searchCondition = localStorage.getItem('searchCondition') || "{}";
        state.searchCondition = JSON.parse(searchCondition)
      }
      return state.searchCondition
    },
    getOrder: state => {
      if (!state.order.HotelId) {
        var order = localStorage.getItem('order') || '{}';
        state.order = JSON.parse(order)
      }
      return state.order;
    },
    getUserInfo: state => {
      if (!state.userInfo.token || state.userInfo.token === "") {
        var userInfo = localStorage.getItem('userInfo') || '{ "token": "", "username": "" }';
        state.userInfo = JSON.parse(userInfo)
      }
      return state.userInfo;
    }
  },
  mutations: {
    userInfoChange(state, user) {
      state.userInfo = user;
      localStorage.setItem('userInfo', JSON.stringify(state.userInfo));
    },
    removeUserInfo(state) {
      state.userInfo = { token: "", username: "" };
      localStorage.removeItem('userInfo');
    },
    searchConditionUpdate(state, newCondition) {
      state.searchCondition = newCondition
      localStorage.setItem('searchCondition', JSON.stringify(state.searchCondition));
    },
    addCheckoutList(state, newHotelRoom) {
      var hotelKey = "Room" + newHotelRoom.HotelId;
      state.checkoutList[hotelKey] = state.checkoutList[hotelKey] || {};
      var item = state.checkoutList[hotelKey];
      var roomTypeKey = "Type" + newHotelRoom.RoomType;
      item[roomTypeKey] = item[roomTypeKey] || {};
      item[roomTypeKey].Quantiy = (item[roomTypeKey].Quantiy || 0) + 1;
      item[roomTypeKey].HotelId = newHotelRoom.HotelId;
      item[roomTypeKey].Price = newHotelRoom.Price;
      item[roomTypeKey].startDate = newHotelRoom.startDate;
      item[roomTypeKey].endDate = newHotelRoom.endDate;
      state.checkoutLen = Object.keys(state.checkoutList).length;
      localStorage.setItem('checkoutList', JSON.stringify(state.checkoutList));
    },
    deleteCheckoutList(state, newHotel) {
      delete state.checkoutList["Room" + newHotel.id]
      localStorage.setItem('checkoutList', JSON.stringify(state.checkoutList));
    },
    updateOrder(state, orderItem) {
      var newOrder = {
        HotelId: orderItem.HotelId,
        startDate: orderItem.startDate,
        endDate: orderItem.endDate,
        rooms: {}
      }
      newOrder.rooms["type" + orderItem.RoomType] = {
        Quantity: orderItem.Quantity
      }
      state.order = newOrder;
      localStorage.setItem('order', JSON.stringify(state.order));
    },
    updateOrderDetail(state, roomItem) {
      var rooms = state.order.rooms;
      rooms["type" + roomItem.RoomType] = rooms["type" + roomItem.RoomType] || {};
      rooms["type" + roomItem.RoomType].Quantity = roomItem.Quantity;

      state.order.rooms = rooms;
      localStorage.setItem('order', JSON.stringify(state.order));
    },
    updateOrderId(state, orderId) {
      state.order.OrderId = orderId;
      localStorage.setItem('order', JSON.stringify(state.order));
    },
    removeOrder(state) {
      state.order = {};
      localStorage.removeItem('order');
    }
  },
  actions: {
  },
  modules: {
  }
})
