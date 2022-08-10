<script>
import { setToken } from '@/utils/auth'

export default {
  name: 'AuthRedirect',
  created() {
    const hash = window.location.hash.slice(2)
    const params = []
    const query_url = hash.slice(hash.indexOf('?') + 1)
    const param_array = query_url.split('&')
    for (let i = 0; i < param_array.length; i++) {
      const kv = param_array[i].split('=')
      params[kv[0]] = kv[1]
    }
    if (window.localStorage) {
      console.log(params)
      this.$store.state.token = params['token']
      setToken(params['token'])
      // window.localStorage.setItem('x-admin-oauth-code', 'abc')
      window.opener.location.reload()
      window.close()
    }
  },
  render: function(h) {
    return h() // avoid warning message
  }
}
</script>
