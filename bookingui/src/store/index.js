import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    userInfo: { signedIn: false },
    searchCondition: {},
    order:{},
    checkoutList: {},
    checkoutLen:0
  },
  getters: {
    getSearchCondition: state => {
      if(!state.searchCondition){
        var searchCondition = localStorage.getItem('searchCondition')||{};
        state.searchCondition = JSON.parse(searchCondition)
      }
      return state.searchCondition 
    },
    getOrder: state => {
      if(!state.order.HotelId){
        var order = localStorage.getItem('order')|| {};
      state.order = JSON.parse(order)
      }
      return state.order;
    },
  },
  mutations: {
    userInfoChange(state, user) {
      state.userInfo.signedIn = user;
      localStorage.setItem('userInfo', JSON.stringify(state.userInfo));
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
      item[roomTypeKey].Quantiy = (item[roomTypeKey].Quantiy || 0) +1;
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
    updateOrder(state, orderItem){
      var newOrder = {
        HotelId: orderItem.HotelId,
        startDate: orderItem.startDate,
        endDate: orderItem.endDate,
        rooms:{}
      }
      newOrder.rooms["type"+orderItem.RoomType] = {
        Quantity: orderItem.Quantity
      }
      state.order = newOrder;
      localStorage.setItem('order', JSON.stringify(state.order));
    },
    updateOrderDetail(state, roomItem){
        var rooms = state.order.rooms;
        rooms["type"+roomItem.RoomType] = rooms["type"+roomItem.RoomType] || {};
        rooms["type"+roomItem.RoomType].Quantity = roomItem.Quantity;

        state.order.rooms = rooms;
        localStorage.setItem('order', JSON.stringify(state.order));
    },
    updateOrderId(state, orderId){
      state.order.OrderId = orderId;
      localStorage.setItem('order', JSON.stringify(state.order));
    },
    removeOrder(state){
      state.order = {};
      localStorage.removeItem('order');
    }
  },
  actions: {
  },
  modules: {
  }
})
