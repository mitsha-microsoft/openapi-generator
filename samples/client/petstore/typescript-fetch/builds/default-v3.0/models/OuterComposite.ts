/* tslint:disable */
/* eslint-disable */
/**
 * OpenAPI Petstore
 * This spec is mainly for testing Petstore server and contains fake endpoints, models. Please do not use this for any other purpose. Special characters: \" \\
 *
 * The version of the OpenAPI document: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

import { mapValues } from '../runtime';
/**
 * 
 * @export
 * @interface OuterComposite
 */
export interface OuterComposite {
    /**
     * 
     * @type {number}
     * @memberof OuterComposite
     */
    myNumber?: number;
    /**
     * 
     * @type {string}
     * @memberof OuterComposite
     */
    myString?: string;
    /**
     * 
     * @type {boolean}
     * @memberof OuterComposite
     */
    myBoolean?: boolean;
}

/**
 * Check if a given object implements the OuterComposite interface.
 */
export function instanceOfOuterComposite(value: object): boolean {
    return true;
}

export function OuterCompositeFromJSON(json: any): OuterComposite {
    return OuterCompositeFromJSONTyped(json, false);
}

export function OuterCompositeFromJSONTyped(json: any, ignoreDiscriminator: boolean): OuterComposite {
    if (json == null) {
        return json;
    }
    return {
        
        'myNumber': json['my_number'] == null ? undefined : json['my_number'],
        'myString': json['my_string'] == null ? undefined : json['my_string'],
        'myBoolean': json['my_boolean'] == null ? undefined : json['my_boolean'],
    };
}

export function OuterCompositeToJSON(value?: OuterComposite | null): any {
    if (value == null) {
        return value;
    }
    return {
        
        'my_number': value['myNumber'],
        'my_string': value['myString'],
        'my_boolean': value['myBoolean'],
    };
}

