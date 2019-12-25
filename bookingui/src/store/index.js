import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    userInfo: { signedIn: false },
    searchCondition: {},
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
      item[roomTypeKey].Price = newHotelRoom.Price;
      state.checkoutLen = Object.keys(state.checkoutList).length;
    },
    deleteCheckoutList(state, newHotel) {
      delete state.checkoutList["Room" + newHotel.id]
    },
  },
  actions: {
  },
  modules: {
  }
})
