# Your dictionary and JSON data
names = {
    'DENVER.UIS::BLKS_EHILL465_01': {
        'name': 'sales_gas_meter',
        'sensors': ['DENVER.UIS:BLKS_EHILL465_01_BSLSHH']
    },
    'fuel_gas_meter': {
        'name': 'saratoga_battery',
        'sensors': ['fuel_gas_meter:flow_rate']
    },
    'lact': {
        'name': 'saratoga_battery',
        'sensors': ['lact:flow_rate']
    },
}

json_data = {
    "data": [
        {
            "facTag": "DENVER.UIS::BLKS_EHILL465_01",
            "parent": "DENVER.UIS::PAD_EHILL465",
            "desc": "Eastern Hills Bulk Sep 01",
            "points": [
                {
                    "tag": "DENVER.UIS:BLKS_EHILL465_01_BSLSHH",
                    "ldesc": "Bulk Sep LSHH",
                    "v": "-124.5182",
                    "t": 1692717738
                },
                # ... Other points
            ]
        },
        {
            "facTag": "DENVER.UIS::CMPGL_EHILL465_01",
            "parent": "DENVER.UIS::PAD_EHILL465",
            "desc": "Easter Hills Comp GL",
            "points": [
                {
                    "tag": "DENVER.UIS:CMPGL_EHILL465_01_COMPSTS",
                    "ldesc": "Compressor Run Status",
                    "v": "0:Stopped",
                    "t": 1692718044
                },
                # ... Other points
            ]
        }
    ]
}


# Iterate over the JSON data
def calculate_sums(json_data, names, delta_minutes):
    result = {}

    for json_obj in json_data['data']:
        name = json_obj['facTag']

        if name in names:
            sums = 0

            for point in json_obj['points']:
                sensor = point['tag']

                if sensor in names[name]['sensors']:
                    try:
                        sums += float(point.get('v', 0))
                    except ValueError:
                        pass

            sums = sums / 1440 * delta_minutes  # Calculate the sum for the current JSON object
            result[name] = result.get(name, 0) + sums  # Accumulate the sum in the result dictionary

    for pad in names:
        if names[pad]['name'] not in result:
            result[names[pad]['name']] = 0

    return result


print(calculate_sums(json_data, names, 20))
