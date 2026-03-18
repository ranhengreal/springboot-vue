import axios from "axios";
import { ElMessage } from "element-plus";

const request = axios.create({
    baseURL: import.meta.env.VITE_APP_API_BASE_URL,
    timeout: 30000, //请求超时时间
});
//request拦截器
request.interceptors.request.use(
    (config) => {
        let token =  localStorage.getItem("token");
        config.headers['token'] = token;
        return config;
    },
    (error) => {
        return Promise.reject(error);
    },
);

//response拦截器
request.interceptors.response.use(
    (response) => {
        return response.data;
    },
    (error) => {
        if (error.response) {
            if (error.response.status == 404) {
                ElMessage.error("未找到请求接口");
            } else if (error.response.status == 500) {
                ElMessage.error("接口调用异常，请查看后台接口日志");
            } else {
                ElMessage.error(error.message);
            }
        } else {
            ElMessage.error(error.message);
        }

        return Promise.reject(error);
    },
);


//download方法
export function download(url, params) {
    //构建查询参数
    let searchParam = new URLSearchParams();
    for (let key in params) {
        searchParam.append(key, params[key]);
    }
    //?name=lisi&nickname=zhangsan
    //通过 url 传递 token
    let user = JSON.parse(sessionStorage.getItem("login_user"));
    if (user) {
        searchParam.append("token", user.token);
    }
    const base_url = import.meta.env.VITE_APP_API_BASE_URL;
    window.location = base_url + url + "?" + searchParam.toString();
}

export default request;