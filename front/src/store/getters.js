const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  token: state => state.user.token,
  username: state => state.user.username,
  userid: state => state.user.userid,
  path: state => state.user.path
}
export default getters
