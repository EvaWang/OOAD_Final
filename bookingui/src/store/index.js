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
  mutations: {
    userInfoChange(state, user) {
      state.userInfo.signedIn = user;
    },
    searchConditionUpdate(state, newCondition) {
      state.searchCondition = newCondition
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
      item[roomTypeKey].StartDate = newHotelRoom.StartDate;
      item[roomTypeKey].EndDate = newHotelRoom.EndDate;
      state.checkoutLen = Object.keys(state.checkoutList).length;
    },
    deleteCheckoutList(state, newHotel) {
      delete state.checkoutList["Room" + newHotel.id]
    },
    updateOrder(state, orderItem){
      console.log(orderItem)
      var newOrder = {
        HotelId: orderItem.HotelId,
        StartDate: orderItem.StartDate,
        EndDate: orderItem.EndDate,
        rooms:{}
      }
      newOrder.rooms["type"+orderItem.RoomType] = {
        Quantity: orderItem.Quantity
      }
      state.order = newOrder;
    },
    updateOrderDetail(state, roomItem){
        var rooms = state.order.rooms;
        rooms["type"+roomItem.RoomType] = rooms["type"+roomItem.RoomType] || {};
        rooms["type"+roomItem.RoomType].Quantity = roomItem.Quantity;

        state.order.rooms = rooms;
    }
  },
  actions: {
  },
  modules: {
  }
})
