import {Button, Form, Input} from "antd";
import {useEffect} from "react";

type FieldType = {
    location?: string;
    date?: string;
};

type MetadataFormProps = {
    readonly date: string;
    readonly location: string;
    readonly onLabel: (date: string, location: string) => Promise<void>
}

function MetadataForm({ date, location, onLabel }: MetadataFormProps){
    const [form] = Form.useForm();

    useEffect(() => {
        form.setFieldsValue({ location, date })
    }, []);

    async function onFinish(values: FieldType){
        await onLabel(values.date ?? "", values.location ?? "")
    }

    return (
        <Form
            layout="vertical"
            form={form}
            style={{ maxWidth: 600 }}
            autoComplete="off"
            onFinish={onFinish}
            requiredMark="optional"
        >
            <Form.Item<FieldType> label="Location" name="location">
                <Input placeholder="Location" />
            </Form.Item>
            <Form.Item<FieldType> label="Date" name="date">
                <Input placeholder="dd/mm/yyyy" />
            </Form.Item>
            <Form.Item style={{paddingTop: '1rem'}}>
                <Button type="primary" htmlType="submit">Label Photo</Button>
            </Form.Item>
        </Form>
    )
}

export default MetadataForm;