import {derived, writable} from "svelte/store";
import type {Writable, Readable } from "svelte/types/runtime/store";

export interface SearchStoreModel<T extends Record<PropertyKey, any>> {
    data: T[]
    filtered: T[]
    paged: T[]
    search: string
    pageSize: number
    currentPage: number
    pageCount: number
    itemBegin: number
    itemEnd: number

}

export interface SearchStore<T extends Record<PropertyKey, any>> extends Writable<T>, SearchStoreModel<T> {
    nextPage(): void;
    prevPage(): void;
    setPage(this: void, value: number): void;
}

export interface SearchTableStoreModel<T extends Record<PropertyKey, any>> {
    data: T[]
    filtered: T[]
    paged: T[]
    pageSize: number
    pageCount: number
    itemBegin: number
    itemEnd: number
}

export interface SearchTableStore<T extends Record<PropertyKey, any>> extends Readable<SearchTableStoreModel<T>>, SearchTableStoreModel<T> {
    nextPage(): void
    prevPage(): void
    setPage(this: void, value: number): void

}

export interface SearchTableInputStore {
    search: Writable<string>
}

export type PageInfo = {
    page: number

    count: number
}

export const createSearchTableStore = <T extends Record<PropertyKey, any>>(stores: Readable<any>, fn: (values: any) => T[], _pageSize: number = 5) => {
    const dataStore: Readable<T[]> = derived<any, T[]>(stores, fn);
    const search: Writable<string> = writable<string>('');
    const currentPage: Writable<PageInfo> = writable<PageInfo>({ page: 1, count: 1 });


    const store = derived<[Readable<T[]>, Readable<string>, Readable<PageInfo>], SearchStoreModel<T>>(
        [dataStore, search, currentPage],
        // @ts-ignore
        ([$d, $s, $p]) => {
            const _filtered = $d.filter(item => item.searchTerms.toLowerCase().includes($s.toLocaleLowerCase()));
            const _pageCount = Math.ceil(_filtered.length / _pageSize);
            $p.count = _pageCount;

            const _paged = _filtered.slice(($p.page - 1) * _pageSize, ($p.page - 1) * _pageSize + _pageSize);
            const _itemEnd = Math.min($p.page * _pageSize, _filtered.length);
            const _itemBegin = 1 + ($p.page -1) * _pageSize;
            return {
                data: dataStore,
                filtered: _filtered,
                pageSize: _pageSize,
                paged: _paged,
                pageCount: _pageCount,
                itemBegin: _itemBegin,
                itemEnd: _itemEnd,
            }
        }
    );
    const { subscribe } = store;
    return {
        subscribe,
        search,
        nextPage: () => currentPage.update(u => {
            if (u.page + 1 <= u.count) {
                u.page = u.page + 1;
            }
            return u;
        } ),
        prevPage: () => currentPage.update(u => {
            if (u.page - 1 > 0) {
                u.page = u.page - 1;
            }
            return u;
        } ),
        setPage: (i: number) => currentPage.update( u => {
            u.page = i;
            return u;
        }),
    }
}
