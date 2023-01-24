grammar GDBMI;

output:
    ( out_of_band_record )* result_record? '(gdb)' nl;

result_record:
    TOKEN? '^' result_class ( ',' result )* nl;

out_of_band_record:
    async_record | stream_record;

async_record:
    exec_async_output | status_async_output | notify_async_output;

exec_async_output:
    TOKEN? '*' async_output nl;

status_async_output:
    TOKEN? '+' async_output nl;

notify_async_output:
    TOKEN? '=' async_output nl;

async_output:
    async_class ( ',' result )*;

result_class:
    'done' | 'running' | 'connected' | 'error' | 'exit';

async_class:
    'stopped';

result:
    variable '=' value;

variable:
    STRING;

value:
    c_string | tuple | list;

tuple:
    '{}' | '{' result ( ',' result )* '}';

list:
    '[]' | '[' value ( ',' value )* ']' | '[' result ( ',' result )* ']';

stream_record:
    console_stream_output | target_stream_output | log_stream_output;

console_stream_output:
    '~' c_string nl;

target_stream_output:
    '@' c_string nl;

log_stream_output:
    '&' c_string nl;

nl:
    '\n' | '\n\r';

c_string:
    '"' STRING '"';

STRING:
    (.)+?;

TOKEN:
    [0-9]+;