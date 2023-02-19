
function table_get_rows(table) {
    const tbody = table.getElementsByTagName("tbody")[0]||table;
    let result = [];
    for (let child of tbody.children) {
        if (child.localName === "tr") {
            if (child.getElementsByTagName("td").length > 0) {
                result.push(child);
            }
        }
    }
    return result;
}

function table_pagination_button(symbol, index, config, disabled, active, first, last) {
    let li = document.createElement("li");
    let a  = document.createElement("a");
    a.href = "#";
    a.innerHTML = symbol;
    if (active) {
        a.className = "inline-block px-6 py-2.5 bg-blue-600 text-white font-medium text-xs leading-tight uppercase hover:bg-blue-700 focus:bg-blue-700 focus:outline-none focus:ring-0 active:bg-blue-800 transition duration-150 ease-in-out";
    } else {
        if (first) {
            a.className = "rounded-l inline-block px-6 py-2.5 bg-blue-400 text-white font-medium text-xs leading-tight uppercase hover:bg-blue-700 focus:bg-blue-700 focus:outline-none focus:ring-0 active:bg-blue-800 transition duration-150 ease-in-out";
        } else if (last) {
            a.className="rounded-r inline-block px-6 py-2.5 bg-blue-400 text-white font-medium text-xs leading-tight uppercase hover:bg-blue-700 focus:bg-blue-700 focus:outline-none focus:ring-0 active:bg-blue-800 transition duration-150 ease-in-out"
        } else {
            a.className = "inline-block px-6 py-2.5 bg-blue-400 text-white font-medium text-xs leading-tight uppercase hover:bg-blue-700 focus:bg-blue-700 focus:outline-none focus:ring-0 active:bg-blue-800 transition duration-150 ease-in-out";
        }
    }
    a.addEventListener("click", function (event) {
        event.preventDefault();
        this.parentNode.click();
        return false;
    }, false);
    li.appendChild(a);

    let classes = [];
    if (disabled) {
        classes.push("disabled");
    }
    if (active) {
        classes.push("active");
    }
    li.className = classes.join(" ");
    li.addEventListener("click", function () {
        if (this.className.split(" ").indexOf("disabled") === -1) {
            config.page = index;
            table_pagination(config);
        }
    }, false);
    return li;
}


function table_pagination(config) {
    let rows = table_get_rows(config.table);

    let rows_per_page = config.rows_per_page;
    let page = config.page;
    let pages = (rows_per_page > 0)? Math.ceil(rows.length / rows_per_page):1;

    // hide none active pages
    for (let i=0;i<rows.length;i++) {
        if (typeof rows[i]["data-display"] == "undefined") {
            rows[i]["data-display"] = rows[i].style.display||"";
        }
        if (rows_per_page > 0) {
            if (i < page*rows_per_page && i >= (page-1)*rows_per_page) {
                rows[i].style.display = rows[i]["data-display"];
            } else {
                rows[i].style.display = "none";
            }
        } else {
            rows[i].style.display = rows[i]["data-display"];
        }
    }

    // navigator
    let nav = config.navigator;
    nav.innerHTML = '';
    nav.appendChild(table_pagination_button("&laquo;", (page>1?page-1:1), config, (page === 1), false, true, false));
    for (let i=1;i<=pages;i++) {
        nav.appendChild(table_pagination_button(i, i, config, false, (page === i), false, false));
    }
    nav.appendChild(table_pagination_button("&raquo;", (pages>page?page+1:page), config, (page === pages), false, false, true));

    // update text
    config.text.innerHTML = "Showing " + (((page-1)*rows_per_page)+1)
        + "-" + (rows.length<page*rows_per_page||rows_per_page==="0"?rows.length:page*rows_per_page)
        + " of " + rows.length;

    // update select page count
    let options = [{value:5,text:'5'},{value:10,text:'10'},{value:20,text:'20'},{value:50,text:'50'},{value: 0,text:'All'}];
    let select = config.select;
    if (select.length === 0) {
        options.forEach(option => {
            let o = document.createElement("option");
            o.value = option.value;
            o.text = option.text;
            select.appendChild(o);
        });
    }
    select.value = rows_per_page;
    select.addEventListener("change", function () {
        config.rows_per_page = this.value;
        table_pagination(config);
    }, false);

}
