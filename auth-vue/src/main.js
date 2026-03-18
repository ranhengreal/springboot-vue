import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from "@element-plus/icons-vue"
import zhCn from "element-plus/es/locale/lang/zh-cn";
import {createPinia} from 'pinia'
import hasPermi from "@/directive/hasPermi.js";

const app = createApp(App)
const pina = createPinia();
app.use(pina)
app.use(router)
app.directive("hasPermi", hasPermi);
app.use(ElementPlus,{
    locale:zhCn
})
app.config.globalProperties.$icons = [];
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.config.globalProperties.$icons.push(key);
    app.component(key, component);
}
app.mount('#app')
