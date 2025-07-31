import { check } from 'k6';
import http from 'k6/http';

export const options = {
    vus: 100,
    duration: '10s',
};

export default function () {
    const keyword = "하나CQ";
    const limit = 100;
    const offset = 10;
    // const url = `http://localhost:8080/api/data-table/search-pageable?keyword=${encodeURIComponent(keyword)}&page=${page}&size=${size}`;
    // const url = `http://localhost:8080/api/data-table/search?keyword=${encodeURIComponent(keyword)}`;
    const url = `http://localhost:8080/api/data-table/search-limit-offset?keyword=${encodeURIComponent(keyword)}&limit=${limit}&offset=${offset}`;

    const res = http.get(url);

    check(res, {
        'status is 200': (r) => r.status === 200,
        'response is not empty': (r) => r.body && r.body.length > 2,
    });
}