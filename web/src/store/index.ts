import { createStore } from 'vuex';
declare let SessionStorage: any;
const USER = "USER";
const store =  createStore({
  state: {
    //初始的时候就应该去缓存里面取一下 如果获取不到我们就加上空对象
    //这样刷新的时候就会加载store 去缓存里面获取一下
    //获取不到才是一个空对象
    user:SessionStorage.get("USER") || {}
  },
  mutations: {
    setUser(state, user ){
      //当登录的时候 除了给store赋值 同时把user放进缓存
      //当我刷新就去缓存把变量再次取出来
      state.user = user ;
      SessionStorage.set(USER,user);
    }
  },
  actions: {
  },
  modules: {
  }
})

export default store;