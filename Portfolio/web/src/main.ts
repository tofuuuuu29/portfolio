import '@/assets/css/index.css'
import 'boxicons/css/boxicons.min.css'

import {createApp} from 'vue'
import {createPinia} from 'pinia'

import App from '@/App.vue'
import router from '@/router'

import {reveal} from '@/directives/reveal.ts'


const app = createApp(App)

app.use(createPinia())
app.use(router)

app.mount('#app')

app.directive('reveal', reveal)   // add before app.mount(...)