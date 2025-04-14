import {GetProp, message, Upload, UploadProps} from "antd";
import {UploadOutlined} from '@ant-design/icons';
import useUserStore from "../../stores/user-store.ts";

export type FileType = Parameters<GetProp<UploadProps, 'beforeUpload'>>[0];

const getBase64 = (file: FileType): Promise<string> =>
    new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => resolve(reader.result as string);
        reader.onerror = (error) => reject(error);
    });

export type Metadata = {
    location: LocationMetadata;
    date: string
}

export type LocationMetadata = {
    latitude: string;
    longitude: string;
}

type UploaderProps = {
    readonly setImage: (image: string) => void;
    readonly setFile: (file: FileType | null) => void;
    readonly setMetadata: (metadata: Metadata) => void;
}

function Uploader({ setImage, setFile, setMetadata }: UploaderProps) {
    const { Dragger } = Upload;
    const userStore = useUserStore();
    const [messageApi, contextHolder] = message.useMessage();

    const props: UploadProps = {
        name: 'file',
        action: '/api/photo/metadata',
        headers: {
            authorization: `Bearer ${userStore.token}`,
        },
        onChange(info) {
            const file = info.file;
            const { status } = info.file;
            if (status !== 'uploading') {
                console.log(info.file, info.fileList);
            }

            (async () => {
                if (status === 'done') {
                    messageApi.success(`${info.file.name} file uploaded successfully.`);

                    if (!file.url && !file.preview) {
                        file.preview = await getBase64(file.originFileObj as FileType);
                    }

                    setImage(file.url ?? (file.preview as string));
                    setFile(file.originFileObj as FileType);
                    setMetadata(file.response)
                } else if (status === 'error') {
                    messageApi.error(`${info.file.name} file upload failed.`);
                }
            })();
        },
        onDrop(e) {
            console.log('Dropped files', e.dataTransfer.files);
        },
    };

    return (
        <Dragger {...props} style={{ display: "block", width: "50rem", margin: "5rem auto"}} showUploadList={false}>
            <p className="ant-upload-drag-icon">
                <UploadOutlined />
            </p>
            <p className="ant-upload-text">Click or drag file to this area to upload</p>
        </Dragger>
    )
}

export default Uploader;