import useUserStore from "../stores/user-store.ts";

type RequestInitWithAnyBody = Partial<Omit<RequestInit, 'body'> & {
    body: any;
}>;


const useFetch = () => {
    const token = useUserStore((state) => state.token);

    return async (url: string, options: RequestInitWithAnyBody = {}): Promise<Response> => {
        const headers = new Headers(options.headers || {});
        if (!headers.has("Content-Type")) {
            headers.set("Content-Type", "application/json");
        }
        if (token) {
            headers.set("Authorization", `Bearer ${token}`);
        }

        if (options.body) {
            options.body = JSON.stringify(options.body);
        }

        const response = await fetch(url, {
            method: options.method ?? "GET",
            ...options,
            headers,
            credentials: "omit"
        });

        if (response.status === 401) {
            location.href = "/login";
        }

        return response;
    };
};

export default useFetch;