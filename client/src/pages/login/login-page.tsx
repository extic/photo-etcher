import styles from "./login-page.module.scss";
import {useState} from "react";
import useUserStore from "../../stores/user-store.ts";
import clsx from "clsx";
import {useNavigate} from "react-router";
import useFetch from "../../utils/use-fetch.ts";
import {Button, Form, Input, theme} from 'antd';

type FieldType = {
    username?: string;
    password?: string;
    remember?: string;
};

function LoginPageNew() {
    const fetch = useFetch();
    const userStore = useUserStore();
    const [errorMessage, setErrorMessage] = useState<string[]>([]);
    const navigate = useNavigate();
    const [form] = Form.useForm();
    const [passwordVisible, setPasswordVisible] = useState(false);

    const {
        token: { colorError },
    } = theme.useToken();

    async function onFinish(values: FieldType){
        setErrorMessage([]);

        const response = await fetch("/api/settings/auth/login", {
            method: "POST",
            body: values
        });
        if (response.ok) {
            const user = await response.json();
            const token = (response.headers.get("authorization") ?? "").replace(/^Bearer\s/, "");
            userStore.login(user.firstName, user.lastName, token, user.roles)
            navigate("/main")
            return;
        }
        if (response.status === 401) {
            setErrorMessage(["Incorrect username or password", "Please try again"])
            return;
        }

        setErrorMessage(["Something went wrong", "Please try again later"])
    };

    return (
        <div className={styles.page}>
            <div className={styles.loginContainer}>
                <Form
                    layout="vertical"
                    form={form}
                    style={{ maxWidth: 600 }}
                    autoComplete="off"
                    onFinish={onFinish}
                    onFieldsChange={() => setErrorMessage([])}
                    requiredMark="optional"
                >
                    <h1 className={styles.title}>Welcome</h1>
                    <p className={styles.subTitle}>Login to your account to continue</p>

                    <Form.Item<FieldType>
                        label="Username"
                        name="username"
                        rules={[{ required: true, message: 'Please input your username!' }]}
                    >
                        <Input placeholder="Username" />
                    </Form.Item>
                    <Form.Item<FieldType>
                        label="Password"
                        name="password"
                        rules={[{ required: true, message: 'Please input your password!' }]}
                    >
                        <Input.Password placeholder="Password" visibilityToggle={{ visible: passwordVisible, onVisibleChange: setPasswordVisible }}/>
                    </Form.Item>
                    <div style={{color: colorError}} className={clsx(styles.errorMessage, errorMessage.length > 0 && styles.showError)}>
                        {errorMessage.map(item => (<div key={item}>{item}</div>))}
                    </div>
                    <Form.Item style={{paddingTop: '1rem'}}>
                        <Button type="primary" htmlType="submit">Login</Button>
                    </Form.Item>
                </Form>
            </div>
        </div>
    )
}

export default LoginPageNew;