import {Flex, Image} from "antd";
import {useState} from "react";
import Uploader, {FileType, Metadata} from "./uploader.tsx";
import useUserStore from "../../stores/user-store.ts";
import MetadataForm from "./metadata-form.tsx";

function ProcessingPage(){
    const [image, setImage] = useState('');
    const [file, setFile] = useState<FileType | null>(null);
    const [date, setDate] = useState("");
    const [location, setLocation] = useState("");
    const userStore = useUserStore();

    async function onLabel(date: string, location: string) {
        const formData = new FormData();
        formData.append('file', file as Blob);
        formData.append('location', location);
        formData.append('date', date);

        const response = await fetch('api/photo/label', {
            method: 'POST',
            headers: {
                Authorization: `Bearer ${userStore.token}`
            },
            body: formData
        });

        if (!response.ok) {
            throw new Error('Failed to label the photo');
        }

        const blob = await response.blob();
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'labeled-image.png';
        a.click();
        window.URL.revokeObjectURL(url);
    }

    return <>
        <h1 style={{textAlign: "center"}}>Welcome to the Photo Metadata Labeler</h1>
        {!image && <Uploader setImage={setImage} setFile={setFile} setMetadata={(metadata: Metadata) => {
            setDate(metadata.date);
            setLocation(metadata.location.latitude + ", " + metadata.location.longitude);
        }}/>}

        {image && (
            <Flex gap={"2rem"}>
                <div>
                    <p>Original Photo:</p>
                    <Image
                        wrapperStyle={{ maxWidth: '50rem' }}
                        preview={false}
                        src={image}
                    />
                </div>
                <div>
                    <p>Metadata:</p>
                    <MetadataForm date={date} location={location} onLabel={onLabel}/>
                </div>
            </Flex>
        )}
    </>
}

export default ProcessingPage;